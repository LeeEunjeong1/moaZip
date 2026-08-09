package com.moazip.feature.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moazip.core.ui.theme.MoaZipPalette
import com.moazip.core.ui.theme.MoaZipTheme
import com.moazip.feature.settings.contract.SettingsIntent
import com.moazip.feature.settings.contract.SettingsMemberRole
import com.moazip.feature.settings.contract.SettingsMemberUiModel
import com.moazip.feature.settings.contract.SettingsState
import com.moazip.feature.settings.ui.component.SettingsActionCard
import com.moazip.feature.settings.ui.component.SettingsHeader
import com.moazip.feature.settings.ui.component.SettingsHomeCard
import com.moazip.feature.settings.ui.component.SettingsMemberCard
import com.moazip.feature.settings.ui.component.SettingsLoading
import com.moazip.feature.settings.ui.component.SettingsErrorContent
import com.moazip.feature.settings.contract.SettingsError

@Composable
fun SettingsScreen(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MoaZipPalette.Cream50)
            .padding(horizontal = 24.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item { SettingsHeader() }
        when {
            state.isLoading -> item { SettingsLoading() }
            state.error == SettingsError.UNAUTHENTICATED || state.error == SettingsError.LOAD_FAILED -> {
                item {
                    SettingsErrorContent(
                        error = checkNotNull(state.error),
                        onRetry = { onIntent(SettingsIntent.RetryClicked) },
                    )
                }
            }
            else -> {
                item { SettingsHomeCard(householdName = state.householdName) }
                item { SettingsMemberCard(members = state.members) }
                item {
                    SettingsActionCard(
                        inviteCode = state.inviteCode,
                        canReissueInviteCode = state.canReissueInviteCode,
                        isReissuingInviteCode = state.isReissuingInviteCode,
                        reissueFailed = state.error == SettingsError.REISSUE_FAILED,
                        onReissueInviteCode = { onIntent(SettingsIntent.ReissueInviteCodeClicked) },
                        onLogout = { onIntent(SettingsIntent.LogoutClicked) },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 760)
@Composable
private fun SettingsScreenPreview() {
    MoaZipTheme {
        SettingsScreen(
            state = SettingsState(
                householdName = "은정 & 재웅의 집",
                members = listOf(
                    SettingsMemberUiModel("1", "은정", SettingsMemberRole.OWNER),
                    SettingsMemberUiModel("2", "재웅", SettingsMemberRole.MEMBER),
                ),
                inviteCode = "MZ-V89D",
                canReissueInviteCode = true,
                isLoading = false,
            ),
            onIntent = {},
        )
    }
}
