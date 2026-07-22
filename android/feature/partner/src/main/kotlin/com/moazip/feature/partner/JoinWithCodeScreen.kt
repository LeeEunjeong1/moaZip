package com.moazip.feature.partner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moazip.core.ui.component.MoaZipButton
import com.moazip.core.ui.component.MoaZipTextField
import com.moazip.core.ui.theme.MoaZipPalette

@Composable
fun JoinWithCodeRoute(
    viewModel: JoinWithCodeViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    JoinWithCodeScreen(state, viewModel::onIntent, onBack, modifier)
}

@Composable
fun JoinWithCodeScreen(
    state: JoinWithCodeState,
    onIntent: (JoinWithCodeIntent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MoaZipPalette.Cream50)
            .padding(horizontal = 24.dp),
    ) {
        Spacer(Modifier.height(28.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) {
                Text(stringResource(R.string.common_back), color = MoaZipPalette.Gray900)
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.join_code_title),
            color = MoaZipPalette.Gray900,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.join_code_description),
            color = MoaZipPalette.Gray500,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(32.dp))
        Text(
            text = stringResource(R.string.join_code_label),
            color = MoaZipPalette.Gray900,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(10.dp))
        MoaZipTextField(
            value = state.code,
            onValueChange = { onIntent(JoinWithCodeIntent.CodeChanged(it)) },
            placeholder = stringResource(R.string.join_code_placeholder),
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.join_code_hint),
            color = MoaZipPalette.Gray500,
            style = MaterialTheme.typography.bodySmall,
        )
        Spacer(Modifier.weight(1f))
        MoaZipButton(
            text = stringResource(R.string.join_code_button),
            onClick = { onIntent(JoinWithCodeIntent.JoinClicked) },
            enabled = state.canJoin,
        )
        Spacer(Modifier.height(48.dp))
    }
}
