package com.x1unix.cashlytics.core.providers

import com.x1unix.cashlytics.core.payments.PaymentEvent

interface MessageParser {
    abstract val sender: String
    abstract fun parseMessage(message: String): PaymentEvent
}