package com.moazip.app.navigation

internal enum class MainTab(
    val route: String,
) {
    Home(AppRoute.Dashboard),
    Assets("assets"),
    Add(AppRoute.AddAsset),
    Records(AppRoute.Records),
    Settings(AppRoute.Settings),
}
