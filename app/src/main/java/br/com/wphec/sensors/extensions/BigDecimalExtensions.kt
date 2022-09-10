package br.com.wphec.sensors.extensions

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

fun BigDecimal.formatCurrencyBrazil(): String {
    val format: NumberFormat = NumberFormat
        .getCurrencyInstance(Locale("pt", "br"))
    return format.format(this)
}