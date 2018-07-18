package com.x1unix.cashlytics.core

import android.content.Context
import com.x1unix.cashlytics.core.messages.BankingMessagesService
import com.x1unix.cashlytics.core.providers.MessageProcessor
import com.x1unix.cashlytics.core.providers.kredobank.KredoMessageParser
import com.x1unix.cashlytics.core.services.HistoryService
import com.x1unix.cashlytics.core.services.WalletService
import com.x1unix.cashlytics.core.storage.Storage

/**
 * Facade for accessing core Cashlytics core services
 *
 * @param context Android application context
 */
class ServiceContainer(val context: Context) {
    /**
     * SMS Message processor
     */
    val messageProcessor = MessageProcessor.withHandlers(
            KredoMessageParser()
    )

    /**
     * Storage container, provides access to data
     */
    val storage = Storage.withDefaults(context)

    /**
     * SMS messages service
     */
    val messages = BankingMessagesService(context, messageProcessor)


    /**
     * Provides access to wallets information
     */
    val wallets = WalletService(storage.wallet, messageProcessor, storage.history)

    /**
     * Provides access to wallet history
     */
    val history = HistoryService(storage.history)
}