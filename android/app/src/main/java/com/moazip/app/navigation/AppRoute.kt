package com.moazip.app.navigation

internal object AppRoute {
    const val Login = "login"
    const val HouseholdGate = "household_gate"
    const val CreateHome = "create_home"
    const val Dashboard = "dashboard"
    const val InvitePartner = "invite_partner"
    const val InviteCodeArgument = "inviteCode"
    const val JoinWithCode = "join_with_code"
    const val AddAsset = "add_asset"
    const val EditAsset = "edit_asset"
    const val AssetIdArgument = "assetId"
    const val EditAssetPattern = "$EditAsset/{$AssetIdArgument}"
    const val Records = "records"
    const val Settings = "settings"
    const val MonthlyBudget = "monthly_budget"
    const val EditMonthlyBudget = "edit_monthly_budget"

    const val InvitePartnerPattern = "$InvitePartner/{$InviteCodeArgument}"

    fun invitePartner(inviteCode: String) = "$InvitePartner/$inviteCode"
    fun editAsset(assetId: String) = "$EditAsset/$assetId"
}
