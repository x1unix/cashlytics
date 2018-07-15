package com.x1unix.cashlytics.core

import android.content.Context
import com.x1unix.cashlytics.core.messages.BankingMessagesService
import com.x1unix.cashlytics.core.providers.MessageProcessor
import com.x1unix.cashlytics.core.providers.kredobank.KredoMessageParser
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
    private val messageProcessor = MessageProcessor.withHandlers(
            KredoMessageParser()
    )

    /**
     * SMS messages service
     */
    val messages = BankingMessagesService(context, messageProcessor)

    /**
     * Storage container, provides access to data
     */
    val storage = Storage.withDefaults(context)
}