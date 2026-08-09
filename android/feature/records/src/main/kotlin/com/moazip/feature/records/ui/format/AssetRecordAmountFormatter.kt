package com.moazip.feature.records.ui.format

import java.text.NumberFormat
import java.util.Locale

internal fun Long.toKoreanAmount(): String {
    val absolute = kotlin.math.abs(this)
    val prefix = if (this < 0) "-" else ""
    val eok = absolute / 100_000_000
    val man = (absolute % 100_000_000) / 10_000
    val formattedMan = NumberFormat.getNumberInstance(Locale.KOREA).format(man)
    return when {
        eok > 0 && man > 0 -> "${prefix}${eok}억 ${formattedMan}만원"
        eok > 0 -> "${prefix}${eok}억원"
        else -> "${prefix}${formattedMan}만원"
    }
}
