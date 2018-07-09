package com.x1unix.cashlytics.core

import android.content.Context
import com.x1unix.cashlytics.core.messages.BankingMessagesService
import com.x1unix.cashlytics.core.providers.MessageProcessor
import com.x1unix.cashlytics.core.providers.kredobank.KredoMessageParser

object Cashlytics {
    lateinit var messages: BankingMessagesService
    private val messageProcessor = MessageProcessor()

    fun init(context: Context) {
        messageProcessor.registerHandlers(KredoMessageParser())
        messages = BankingMessagesService(context, messageProcessor)
    }

}