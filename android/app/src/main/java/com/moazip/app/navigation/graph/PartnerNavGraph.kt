package com.moazip.app.navigation.graph

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.moazip.app.navigation.AppRoute
import com.moazip.feature.partner.createhome.CreateHomeRoute
import com.moazip.feature.partner.createhome.CreateHomeViewModel
import com.moazip.feature.partner.createhome.contract.CreateHomeEffect
import com.moazip.feature.partner.invitepartner.InvitePartnerRoute
import com.moazip.feature.partner.invitepartner.InvitePartnerViewModel
import com.moazip.feature.partner.invitepartner.contract.InvitePartnerEffect
import com.moazip.feature.partner.joinwithcode.JoinWithCodeRoute
import com.moazip.feature.partner.joinwithcode.JoinWithCodeViewModel
import com.moazip.feature.partner.joinwithcode.contract.JoinWithCodeEffect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal fun NavGraphBuilder.partnerGraph(navController: NavHostController) {
    composable(AppRoute.CreateHome) {
        val viewModel: CreateHomeViewModel = hiltViewModel()
        LaunchedEffect(viewModel) {
            viewModel.effect
                .onEach { effect ->
                    when (effect) {
                        is CreateHomeEffect.NavigateToInvitePartner -> {
                            navController.navigate(AppRoute.invitePartner(effect.inviteCode))
                        }
                        CreateHomeEffect.NavigateToJoinWithCode -> {
                            navController.navigate(AppRoute.JoinWithCode)
                        }
                    }
                }
                .launchIn(this)
        }
        CreateHomeRoute(viewModel)
    }

    composable(
        route = AppRoute.InvitePartnerPattern,
        arguments = listOf(
            navArgument(AppRoute.InviteCodeArgument) { type = NavType.StringType },
        ),
    ) {
        val viewModel: InvitePartnerViewModel = hiltViewModel()
        LaunchedEffect(viewModel) {
            viewModel.effect
                .onEach { effect ->
                    when (effect) {
                        InvitePartnerEffect.NavigateToDashboard -> {
                            navController.navigate(AppRoute.Dashboard) {
                                popUpTo(AppRoute.CreateHome) { inclusive = true }
                            }
                        }
                        InvitePartnerEffect.OpenJoinWithCode -> {
                            navController.navigate(AppRoute.JoinWithCode)
                        }
                    }
                }
                .launchIn(this)
        }
        InvitePartnerRoute(viewModel)
    }

    composable(AppRoute.JoinWithCode) {
        val viewModel: JoinWithCodeViewModel = hiltViewModel()
        LaunchedEffect(viewModel) {
            viewModel.effect
                .onEach { effect ->
                    if (effect is JoinWithCodeEffect.NavigateToDashboard) {
                        navController.navigate(AppRoute.Dashboard) {
                            popUpTo(AppRoute.CreateHome) { inclusive = true }
                        }
                    }
                }
                .launchIn(this)
        }
        JoinWithCodeRoute(
            viewModel = viewModel,
            onBack = navController::popBackStack,
        )
    }
}
