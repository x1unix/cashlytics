package com.x1unix.cashlytics.core.payments

enum class PaymentType(val value: String) {
    Unknown("unknown"),
    Internet("internet"),
    Purchase("purchase"),
    Transfer("transfer"),
    Withdrawal("withdrawal"),
    Refill("refill"),
    Debit("debit"),
    Revert("revert");

    companion object {
        fun fromString(value: String): PaymentType {
            try {
                return PaymentType.valueOf(value.capitalize())
            } catch (e: IllegalArgumentException) {
                return PaymentType.Unknown
            }
        }
    }
}