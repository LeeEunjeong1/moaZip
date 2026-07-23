package com.moazip.feature.partner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moazip.core.ui.component.MoaZipButton
import com.moazip.core.ui.component.MoaZipOutlinedButton
import com.moazip.core.ui.component.MoaZipTextField
import com.moazip.core.ui.theme.MoaZipPalette

@Composable
fun CreateHomeRoute(viewModel: CreateHomeViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.state.collectAsState()
    CreateHomeScreen(state = state, onIntent = viewModel::onIntent, modifier = modifier)
}

@Composable
fun CreateHomeScreen(
    state: CreateHomeState,
    onIntent: (CreateHomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MoaZipPalette.Cream50)
            .padding(horizontal = 24.dp),
    ) {
        Spacer(Modifier.height(42.dp))
        Text(
            text = stringResource(R.string.create_home_title),
            color = MoaZipPalette.Gray900,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.create_home_description),
            color = MoaZipPalette.Gray500,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(32.dp))
        Text(
            text = stringResource(R.string.create_home_name_label),
            color = MoaZipPalette.Gray900,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(10.dp))
        MoaZipTextField(
            value = state.homeName,
            onValueChange = { onIntent(CreateHomeIntent.HomeNameChanged(it)) },
            placeholder = stringResource(R.string.create_home_name_placeholder),
        )
        if (state.errorMessage != null) {
            Spacer(Modifier.height(10.dp))
            Text(
                text = state.errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Spacer(Modifier.weight(1f))
        MoaZipButton(
            text = stringResource(
                if (state.isCreating) R.string.create_home_creating else R.string.create_home_button,
            ),
            onClick = { onIntent(CreateHomeIntent.CreateClicked) },
            enabled = state.canCreate,
        )
        Spacer(Modifier.height(16.dp))
        MoaZipOutlinedButton(
            text = stringResource(R.string.invite_partner_join),
            onClick = { onIntent(CreateHomeIntent.JoinWithCodeClicked) },
        )
        Spacer(Modifier.height(48.dp))
    }
}
