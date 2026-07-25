package com.moazip.feature.auth

import com.moazip.feature.auth.contract.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.moazip.core.ui.component.MoaZipButton
import com.moazip.core.ui.component.MoaZipLogo
import com.moazip.core.ui.theme.MoaZipPalette
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginRoute(
    viewModel: LoginViewModel,
    onGoogleLoginRequested: suspend () -> GoogleLoginOutcome,
    onLoginSucceeded: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                LoginEffect.RequestGoogleLogin -> viewModel.onIntent(
                    LoginIntent.GoogleLoginCompleted(onGoogleLoginRequested()),
                )
                LoginEffect.NavigateToPartnerSetup -> onLoginSucceeded()
            }
        }
    }

    LoginScreen(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onIntent: (LoginIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MoaZipPalette.Cream50)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(124.dp))
        MoaZipLogo()
        Spacer(modifier = Modifier.height(29.dp))
        Text(
            text = stringResource(R.string.login_brand_name),
            color = MoaZipPalette.Gray900,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.login_description),
            color = MoaZipPalette.Gray500,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            lineHeight = 26.sp,
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(modifier = Modifier.fillMaxWidth()) {
            MoaZipButton(
                text = stringResource(R.string.login_google_button),
                onClick = { onIntent(LoginIntent.GoogleLoginClicked) },
                enabled = !state.isLoading,
            )
            state.error?.let { error ->
                Text(
                    text = when (error) {
                        LoginError.GoogleLoginFailed -> stringResource(R.string.login_google_error)
                    },
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        Spacer(modifier = Modifier.height(173.dp))
    }
}
