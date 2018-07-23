package com.x1unix.cashlytics.core.providers

import com.x1unix.cashlytics.core.messages.BankingMessagesService
import com.x1unix.cashlytics.core.payments.PaymentEvent
import com.x1unix.cashlytics.core.payments.Wallet

interface MessageParser {
    val brandIcon: Int
    val sender: String
    fun parseMessage(message: String): PaymentEvent
    fun buildWallet(event: PaymentEvent, context: BankingMessagesService): Wallet

    /**
     * Event handler fires when a new event occurs before a wallet data update.
     *
     * @return Should a wallet been updated
     */
    fun onWalletUpdate(newEvent: PaymentEvent): Boolean
}