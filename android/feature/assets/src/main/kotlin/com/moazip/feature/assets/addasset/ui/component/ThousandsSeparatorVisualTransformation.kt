package com.moazip.feature.assets.addasset.ui.component

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

object ThousandsSeparatorVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text
        val formatted = digits.reversed()
            .chunked(3)
            .joinToString(",")
            .reversed()

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return 0
                if (offset >= digits.length) return formatted.length

                var digitCount = 0
                formatted.forEachIndexed { index, character ->
                    if (character.isDigit()) digitCount++
                    if (digitCount == offset) return index + 1
                }
                return formatted.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                return formatted
                    .take(offset.coerceIn(0, formatted.length))
                    .count(Char::isDigit)
            }
        }

        return TransformedText(
            text = AnnotatedString(formatted),
            offsetMapping = offsetMapping,
        )
    }
}
