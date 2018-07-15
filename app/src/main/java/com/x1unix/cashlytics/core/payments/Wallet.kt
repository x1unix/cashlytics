package com.x1unix.cashlytics.core.payments

import org.joda.time.LocalDateTime

data class Wallet (
        val bankName: String,
        val status: Amount,
        val lastUpdated: LocalDateTime,
        val description: String = "SMS",
        var icon: Int = 0,
        var id: Long = 0
) {
    companion object {
        fun fromEvent(event: PaymentEvent): Wallet {
            return Wallet(event.bankName, event.changes.left!!, event.date)
        }
    }

    fun setBrandIconResource(resourceId: Int): Wallet {
        icon = resourceId
        return this
    }
}