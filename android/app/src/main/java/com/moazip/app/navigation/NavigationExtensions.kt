package com.moazip.app.navigation

import androidx.navigation.NavHostController

internal fun NavHostController.navigateToMainTab(tab: MainTab) {
    navigate(tab.route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(AppRoute.Dashboard) {
            saveState = true
        }
    }
}
