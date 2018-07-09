package com.x1unix.cashlytics.core.providers

import com.x1unix.cashlytics.core.exceptions.NoMatchFoundException
import com.x1unix.cashlytics.core.payments.PaymentEvent
import javax.inject.Inject

class MessageProcessor {
    private var handlers = mutableMapOf<String, MessageParser>();

    fun registerHandlers(vararg parsers: MessageParser) {
        for (parser in parsers) {
            handlers[parser.sender] = parser
        }
    }

    fun hasHandlerFor(sender: String): Boolean {
        return handlers.containsKey(sender)
    }

    fun getSupportedHandlers(): MutableSet<String> {
        return handlers.keys
    }

    fun extractPaymentInfoFromMessage(sender: String, message: String): PaymentEvent {
        if (!handlers.containsKey(sender)) {
            throw NoMatchFoundException("No provider available for '$sender")
        }

        val handler = handlers[sender]

        return handler!!.parseMessage(message)
    }
}