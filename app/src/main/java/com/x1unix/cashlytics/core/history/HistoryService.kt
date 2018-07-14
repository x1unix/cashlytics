package com.x1unix.cashlytics.core.history

import com.x1unix.cashlytics.core.messages.BankingMessagesService
import com.x1unix.cashlytics.core.messages.Message
import com.x1unix.cashlytics.core.payments.PaymentEvent
import com.x1unix.cashlytics.core.providers.MessageProcessor

class HistoryService (private val messagesStorage: BankingMessagesService, private val processor: MessageProcessor) {
    fun getAccountHistory(providerName: String): List<PaymentEvent> {
        val parser = processor.getProviderProcessor(providerName)
        return messagesStorage.getProviderMessages(providerName).map({i: Message -> parser.parseMessage(i.contents)}).sortedWith(compareBy<PaymentEvent>({it.date}))
    }
}