package ru.patriotovsky.pricecalculator.models

import java.text.DecimalFormat
import java.text.NumberFormat

data class Card(var price: Float, var amount: Float, var winner: Boolean = false) {
    val result: Float
        get() {
            if (price > 0 && amount > 0) return price / amount
            return 0F
        }

    fun getFormattedResult(): String {
        return formatFloat(result)
    }

    private fun formatFloat(i: Float): String {
        val nf = NumberFormat.getCurrencyInstance()
        val pattern: String = (nf as DecimalFormat).toPattern()
        val newPattern = pattern.replace("\u00A4", "").trim { it <= ' ' }
        val newFormat: NumberFormat = DecimalFormat(newPattern)

        return newFormat.format(i)
    }
}
