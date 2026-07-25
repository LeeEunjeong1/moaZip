package com.moazip.app

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.moazip.feature.assets.AssetsRoute
import com.moazip.feature.auth.LoginRoute
import com.moazip.feature.auth.LoginViewModel
import com.moazip.app.auth.FirebaseGoogleAuthClient
import com.moazip.feature.dashboard.contract.DashboardEffect
import com.moazip.feature.dashboard.DashboardRoute
import com.moazip.feature.dashboard.DashboardViewModel
import com.moazip.feature.partner.createhome.CreateHomeRoute
import com.moazip.feature.partner.createhome.CreateHomeViewModel
import com.moazip.feature.partner.createhome.contract.CreateHomeEffect
import com.moazip.feature.partner.invitepartner.InvitePartnerRoute
import com.moazip.feature.partner.invitepartner.InvitePartnerViewModel
import com.moazip.feature.partner.invitepartner.contract.InvitePartnerEffect
import com.moazip.feature.partner.joinwithcode.JoinWithCodeRoute
import com.moazip.feature.partner.joinwithcode.JoinWithCodeViewModel
import com.moazip.feature.partner.joinwithcode.contract.JoinWithCodeEffect
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.core.ui.theme.MoaZipTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private object Route {
    const val Login = "login"
    const val HouseholdGate = "household_gate"
    const val CreateHome = "create_home"
    const val Dashboard = "dashboard"
    const val InvitePartner = "invite_partner"
    const val InviteCodeArgument = "inviteCode"
    const val JoinWithCode = "join_with_code"
    const val Assets = "assets"

    fun invitePartner(inviteCode: String) = "$InvitePartner/$inviteCode"
}

@Composable
fun MoaZipApp(
    container: AppContainer,
    googleAuthClient: FirebaseGoogleAuthClient,
) {
    val navController = rememberNavController()
    val startDestination = remember(googleAuthClient) {
        if (googleAuthClient.hasAuthenticatedUser()) Route.HouseholdGate else Route.Login
    }

    MoaZipTheme {
        Surface {
            NavHost(navController = navController, startDestination = startDestination) {
                composable(Route.Login) {
                    val loginViewModel: LoginViewModel = viewModel()
                    LoginRoute(
                        viewModel = loginViewModel,
                        onGoogleLoginRequested = googleAuthClient::signIn,
                        onLoginSucceeded = {
                            navController.navigate(Route.HouseholdGate) {
                                popUpTo(Route.Login) { inclusive = true }
                            }
                        },
                    )
                }
                composable(Route.HouseholdGate) {
                    LaunchedEffect(Unit) {
                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                        val hasJoinedHousehold = userId != null && runCatching {
                            container.hasJoinedHouseholdUseCase(userId)
                        }.onFailure { exception ->
                            Log.e("HouseholdGate", "Failed to restore household membership", exception)
                        }.getOrDefault(false)
                        val nextRoute = if (hasJoinedHousehold) {
                            Route.Dashboard
                        } else {
                            Route.CreateHome
                        }
                        navController.navigate(nextRoute) {
                            popUpTo(Route.HouseholdGate) { inclusive = true }
                        }
                    }
                    HouseholdGateScreen()
                }
                composable(Route.CreateHome) {
                    val createHomeViewModel: CreateHomeViewModel = viewModel(
                        factory = viewModelFactory {
                            CreateHomeViewModel(
                                createHouseholdUseCase = container.createHouseholdUseCase,
                                currentUserIdProvider = { FirebaseAuth.getInstance().currentUser?.uid },
                            )
                        },
                    )
                    LaunchedEffect(createHomeViewModel) {
                        createHomeViewModel.effect
                            .onEach { effect ->
                                when (effect) {
                                    is CreateHomeEffect.NavigateToInvitePartner -> {
                                        navController.navigate(Route.invitePartner(effect.inviteCode))
                                    }
                                    CreateHomeEffect.NavigateToJoinWithCode -> {
                                        navController.navigate(Route.JoinWithCode)
                                    }
                                }
                            }
                            .launchIn(this)
                    }
                    CreateHomeRoute(viewModel = createHomeViewModel)
                }
                composable(
                    route = "${Route.InvitePartner}/{${Route.InviteCodeArgument}}",
                    arguments = listOf(navArgument(Route.InviteCodeArgument) { type = NavType.StringType }),
                ) { backStackEntry ->
                    val inviteCode = backStackEntry.arguments?.getString(Route.InviteCodeArgument).orEmpty()
                    val invitePartnerViewModel: InvitePartnerViewModel = viewModel(
                        factory = viewModelFactory {
                            InvitePartnerViewModel(
                                inviteCode = inviteCode,
                                reissueInviteCodeUseCase = container.reissueInviteCodeUseCase,
                                currentUserIdProvider = { FirebaseAuth.getInstance().currentUser?.uid },
                            )
                        },
                    )
                    LaunchedEffect(invitePartnerViewModel) {
                        invitePartnerViewModel.effect
                            .onEach { effect ->
                                when (effect) {
                                    InvitePartnerEffect.NavigateToDashboard -> {
                                        navController.navigate(Route.Dashboard) {
                                            popUpTo(Route.CreateHome) { inclusive = true }
                                        }
                                    }
                                    InvitePartnerEffect.OpenJoinWithCode -> {
                                        navController.navigate(Route.JoinWithCode)
                                    }
                                }
                            }
                            .launchIn(this)
                    }
                    InvitePartnerRoute(viewModel = invitePartnerViewModel)
                }
                composable(Route.JoinWithCode) {
                    val joinWithCodeViewModel: JoinWithCodeViewModel = viewModel(
                        factory = viewModelFactory {
                            JoinWithCodeViewModel(
                                joinHouseholdWithInviteCodeUseCase = container.joinHouseholdWithInviteCodeUseCase,
                                currentUserIdProvider = { FirebaseAuth.getInstance().currentUser?.uid },
                            )
                        },
                    )
                    LaunchedEffect(joinWithCodeViewModel) {
                        joinWithCodeViewModel.effect
                            .onEach { effect ->
                                if (effect is JoinWithCodeEffect.NavigateToDashboard) {
                                    navController.navigate(Route.Dashboard) {
                                        popUpTo(Route.CreateHome) { inclusive = true }
                                    }
                                }
                            }
                            .launchIn(this)
                    }
                    JoinWithCodeRoute(
                        viewModel = joinWithCodeViewModel,
                        onBack = navController::popBackStack,
                    )
                }
                composable(Route.Dashboard) {
                    val dashboardViewModel: DashboardViewModel = viewModel(
                        factory = viewModelFactory {
                            DashboardViewModel(
                                observeDashboardSummary = container.observeDashboardSummary,
                                getLatestInviteCodeUseCase = container.getLatestInviteCodeUseCase,
                                currentUserIdProvider = { FirebaseAuth.getInstance().currentUser?.uid },
                            )
                        },
                    )
                    LaunchedEffect(dashboardViewModel) {
                        dashboardViewModel.effect
                            .onEach { effect ->
                                when (effect) {
                                    DashboardEffect.NavigateToAssets -> navController.navigate(Route.Assets)
                                    is DashboardEffect.NavigateToPartnerInvite -> {
                                        navController.navigate(Route.invitePartner(effect.inviteCode))
                                    }
                                    is DashboardEffect.ShowMessage -> Unit
                                }
                            }
                            .launchIn(this)
                    }
                    DashboardRoute(viewModel = dashboardViewModel)
                }
                composable(Route.Assets) { AssetsRoute() }
            }
        }
    }
}

@Composable
private fun HouseholdGateScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MoaZipPalette.Cream50),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}
