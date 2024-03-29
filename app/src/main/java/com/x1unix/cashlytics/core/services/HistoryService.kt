package com.x1unix.cashlytics.core.services

import com.x1unix.cashlytics.core.payments.PaymentEvent
import com.x1unix.cashlytics.core.storage.repository.HistoryRepository

class HistoryService(private val history: HistoryRepository) {
    /**
     * Returns all events of specified wallet
     */
    fun getWalletTimeline(walletId: String): List<PaymentEvent> {
        return history.getTimeline(walletId)
    }

    /**
     * Imports events to a wallet's history
     */
    fun importData(walletId: String, items: List<PaymentEvent>) {
        val newItems = items.map(fun (e: PaymentEvent): PaymentEvent {
            e.walletId = walletId
            return e
        })

        history.bulkAdd(newItems)
    }
}