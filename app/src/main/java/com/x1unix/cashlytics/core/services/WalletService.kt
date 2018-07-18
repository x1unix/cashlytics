package com.x1unix.cashlytics.core.services

import com.x1unix.cashlytics.core.payments.PaymentEvent
import com.x1unix.cashlytics.core.payments.Wallet
import com.x1unix.cashlytics.core.providers.MessageProcessor
import com.x1unix.cashlytics.core.storage.repository.HistoryRepository
import com.x1unix.cashlytics.core.storage.repository.WalletRepository

class WalletService (private val repository: WalletRepository, private val proc: MessageProcessor, private val history: HistoryRepository) {
    fun getWallets(): List<Wallet> {
        return repository.getAll().map { w: Wallet -> decorateWallet(w) }
    }

    fun decorateWallet(w: Wallet): Wallet {
        val handler = proc.getProviderProcessor(w.bankName)
        w.setBrandIconResource(handler.brandIcon)
        return w
    }

    fun getAll(): List<Wallet> {
        return repository.getAll()
    }

    fun addWalletEvent(w: Wallet, event: PaymentEvent) {
        val walletId = w.id
        event.walletId = walletId

        history.addItem(event)
        repository.updateWalletStatus(walletId, event.date, event.changes.left!!)
    }
}