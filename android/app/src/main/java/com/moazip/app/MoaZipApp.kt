package com.moazip.app

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.moazip.feature.dashboard.DashboardEffect
import com.moazip.feature.dashboard.DashboardRoute
import com.moazip.feature.dashboard.DashboardViewModel
import com.moazip.feature.partner.InvitePartnerEffect
import com.moazip.feature.partner.InvitePartnerRoute
import com.moazip.feature.partner.InvitePartnerViewModel
import com.moazip.feature.partner.CreateHomeEffect
import com.moazip.feature.partner.CreateHomeRoute
import com.moazip.feature.partner.CreateHomeViewModel
import com.moazip.feature.partner.JoinWithCodeEffect
import com.moazip.feature.partner.JoinWithCodeRoute
import com.moazip.feature.partner.JoinWithCodeViewModel
import com.moazip.core.ui.theme.MoaZipTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import androidx.compose.runtime.LaunchedEffect

private object Route {
    const val Login = "login"
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
        if (googleAuthClient.hasAuthenticatedUser()) Route.CreateHome else Route.Login
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
                            navController.navigate(Route.CreateHome) {
                                popUpTo(Route.Login) { inclusive = true }
                            }
                        },
                    )
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
                            InvitePartnerViewModel(inviteCode = inviteCode)
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
                    val joinWithCodeViewModel: JoinWithCodeViewModel = viewModel()
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
                            DashboardViewModel(container.observeDashboardSummary)
                        },
                    )
                    LaunchedEffect(dashboardViewModel) {
                        dashboardViewModel.effect
                            .onEach { effect ->
                                if (effect is DashboardEffect.NavigateToAssets) {
                                    navController.navigate(Route.Assets)
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
