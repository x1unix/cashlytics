package com.x1unix.cashlytics.core.payments

import org.joda.time.LocalDateTime

data class Wallet (
        val bankName: String,
        var status: Amount,
        val lastUpdated: LocalDateTime,
        val description: String = "SMS",
        var icon: Int = 0,
        var id: String = ""
) {
    lateinit var sourceEvent: PaymentEvent

    companion object {
        fun fromEvent(event: PaymentEvent): Wallet {
            val v = Wallet(event.bankName, event.changes.left!!, event.date)
            v.sourceEvent = event

            return v
        }
    }

    fun setBrandIconResource(resourceId: Int): Wallet {
        icon = resourceId
        return this
    }
}