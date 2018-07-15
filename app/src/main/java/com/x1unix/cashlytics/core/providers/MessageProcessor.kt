package com.x1unix.cashlytics.core.providers

import android.content.Context
import com.x1unix.cashlytics.core.exceptions.NoMatchFoundException
import com.x1unix.cashlytics.core.payments.PaymentEvent
import com.x1unix.cashlytics.core.payments.Wallet
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

    fun getProviderProcessor(sender: String): MessageParser {
        if (!handlers.containsKey(sender)) {
            throw NoMatchFoundException("No provider available for '$sender")
        }

        return handlers[sender]!!
    }

    fun extractWalletInfoFromMessage(sender: String, message: String): Wallet {
        val handler = getProviderProcessor(sender)
        val event = handler.parseMessage(message)

        return Wallet.fromEvent(event).setBrandIconResource(handler.brandIcon)
    }

    fun extractPaymentInfoFromMessage(sender: String, message: String): PaymentEvent {
        if (!handlers.containsKey(sender)) {
            throw NoMatchFoundException("No provider available for '$sender")
        }

        val handler = handlers[sender]

        return handler!!.parseMessage(message)
    }

    companion object {

        /**
         * Creates a new processor with specified handlers
         */
        fun withHandlers(vararg parsers: MessageParser): MessageProcessor {
            val processor = MessageProcessor()

            processor.registerHandlers(*parsers)

            return processor
        }
    }
}