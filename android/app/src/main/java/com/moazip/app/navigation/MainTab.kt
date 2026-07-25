package com.moazip.app.navigation

internal enum class MainTab(
    val route: String,
) {
    Home("dashboard"),
    Assets("assets"),
    Add("add_asset"),
    Records("records"),
    Settings("settings"),
}
