package com.moazip.app

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moazip.feature.assets.AssetsRoute
import com.moazip.feature.auth.LoginRoute
import com.moazip.feature.auth.LoginViewModel
import com.moazip.feature.dashboard.DashboardEffect
import com.moazip.feature.dashboard.DashboardRoute
import com.moazip.feature.dashboard.DashboardViewModel
import com.moazip.core.ui.theme.MoaZipTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import androidx.compose.runtime.LaunchedEffect

private object Route {
    const val Login = "login"
    const val Dashboard = "dashboard"
    const val Assets = "assets"
}

@Composable
fun MoaZipApp(container: AppContainer) {
    val navController = rememberNavController()

    MoaZipTheme {
        Surface {
            NavHost(navController = navController, startDestination = Route.Login) {
                composable(Route.Login) {
                    val loginViewModel: LoginViewModel = viewModel()
                    LoginRoute(
                        viewModel = loginViewModel,
                        onGoogleLoginRequested = {
                            // Credential Manager and Firebase Auth will be connected next.
                        },
                        onInviteCodeRequested = {
                            // Invite-code flow will be connected when its screen is added.
                        },
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
