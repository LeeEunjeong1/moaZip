package com.moazip.feature.settings.route

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.moazip.feature.settings.R
import com.moazip.feature.settings.contract.SettingsIntent
import com.moazip.feature.settings.contract.SettingsMemberRole
import com.moazip.feature.settings.contract.SettingsMemberUiModel
import com.moazip.feature.settings.contract.SettingsState
import com.moazip.feature.settings.ui.SettingsScreen

@Composable
fun SettingsRoute(modifier: Modifier = Modifier) {
    SettingsScreen(
        state = SettingsState(
            householdName = stringResource(R.string.settings_sample_household_name),
            members = listOf(
                SettingsMemberUiModel(
                    id = "owner",
                    name = stringResource(R.string.settings_sample_owner_name),
                    role = SettingsMemberRole.OWNER,
                ),
                SettingsMemberUiModel(
                    id = "member",
                    name = stringResource(R.string.settings_sample_member_name),
                    role = SettingsMemberRole.MEMBER,
                ),
            ),
        ),
        onIntent = { intent ->
            when (intent) {
                SettingsIntent.ReissueInviteCodeClicked -> Unit
                SettingsIntent.LogoutClicked -> Unit
            }
        },
        modifier = modifier,
    )
}
