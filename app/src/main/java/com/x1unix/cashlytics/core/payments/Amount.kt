package com.x1unix.cashlytics.core.payments

data class Amount(val amount: Double, val currency: String) {
    override fun toString(): String {
        return amount.toString() + " " + currency
    }

    fun isNegative(): Boolean {
        return amount < 0
    }

    companion object {
        fun fromString(str: String): Amount {
            val chunks = str.split(' ')
            return Amount(chunks[0].toDouble(), chunks[1])
        }
    }
}