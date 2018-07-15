package com.x1unix.cashlytics.core.payments

data class Amount(val amount: Double, val currency: String) {
    override fun toString(): String {
        return amount.toString() + " " + currency
    }

    fun isNegative(): Boolean {
        return amount < 0
    }
}