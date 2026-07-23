package com.moazip.feature.partner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moazip.core.ui.component.MoaZipButton
import com.moazip.core.ui.component.MoaZipOutlinedButton
import com.moazip.core.ui.theme.MoaZipPalette

@Composable
fun InvitePartnerRoute(viewModel: InvitePartnerViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.state.collectAsState()
    InvitePartnerScreen(state, viewModel::onIntent, modifier)
}

@Composable
fun InvitePartnerScreen(
    state: InvitePartnerState,
    onIntent: (InvitePartnerIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = modifier.fillMaxSize().background(MoaZipPalette.Cream50).padding(horizontal = 24.dp),
    ) {
        Spacer(Modifier.height(42.dp))
        Text(stringResource(R.string.invite_partner_title), color = MoaZipPalette.Gray900, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text(stringResource(R.string.invite_partner_description), color = MoaZipPalette.Gray500, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(17.dp))

        Card(
            modifier = Modifier.fillMaxWidth().height(170.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MoaZipPalette.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(23.dp))
                Text(stringResource(R.string.invite_partner_code_label), color = MoaZipPalette.Gray500)
                Spacer(Modifier.height(17.dp))
                Text(state.inviteCode, color = MoaZipPalette.Gray900, fontSize = 40.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(15.dp))
                Text(stringResource(R.string.invite_partner_code_expiry), color = MoaZipPalette.Gray500, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(Modifier.height(16.dp))
        MoaZipButton(
            text = stringResource(if (state.isCodeCopied) R.string.invite_partner_copied else R.string.invite_partner_copy),
            onClick = {
                clipboardManager.setText(AnnotatedString(state.inviteCode))
                onIntent(InvitePartnerIntent.CopyCodeClicked)
            },
        )
        Spacer(Modifier.height(16.dp))
        if (state.isCodeCopied) {
            MoaZipButton(
                text = stringResource(R.string.invite_partner_go_dashboard),
                onClick = { onIntent(InvitePartnerIntent.LaterClicked) },
            )
        } else {
            MoaZipOutlinedButton(
                text = stringResource(R.string.invite_partner_later),
                onClick = { onIntent(InvitePartnerIntent.LaterClicked) },
            )
        }
        Spacer(Modifier.height(12.dp))
        MoaZipOutlinedButton(
            text = stringResource(R.string.invite_partner_join),
            onClick = { onIntent(InvitePartnerIntent.JoinWithCodeClicked) },
        )
        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth().background(MoaZipPalette.Yellow50, RoundedCornerShape(18.dp)).padding(18.dp),
        ) {
            Text(stringResource(R.string.invite_partner_permission_title), color = MoaZipPalette.Gray900, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(
                stringResource(R.string.invite_partner_permission_description),
                color = MoaZipPalette.Gray500,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start,
            )
        }
    }
}
