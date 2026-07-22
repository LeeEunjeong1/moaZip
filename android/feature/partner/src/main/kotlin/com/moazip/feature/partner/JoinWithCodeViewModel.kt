package com.moazip.feature.partner

import com.moazip.core.presentation.mvi.MviViewModel
import java.util.Locale

class JoinWithCodeViewModel : MviViewModel<JoinWithCodeIntent, JoinWithCodeState, JoinWithCodeEffect>(
    JoinWithCodeState(),
) {
    override fun onIntent(intent: JoinWithCodeIntent) {
        when (intent) {
            is JoinWithCodeIntent.CodeChanged -> reduce {
                copy(
                    code = intent.code
                        .filter { it.isLetterOrDigit() || it == '-' }
                        .uppercase(Locale.ROOT)
                        .take(CODE_LENGTH),
                )
            }
            JoinWithCodeIntent.JoinClicked -> {
                if (state.value.canJoin) postEffect(JoinWithCodeEffect.NavigateToDashboard)
            }
        }
    }

    private companion object {
        const val CODE_LENGTH = 7
    }
}
