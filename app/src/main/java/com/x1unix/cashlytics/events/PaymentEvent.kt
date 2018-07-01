package com.x1unix.cashlytics.events

import org.joda.time.LocalDateTime

/**
 * Payment event
 */
data class PaymentEvent (val date: LocalDateTime, val metadata: PaymentMetadata, val charged: Int, val balance: Int, val overdraft: Int) {
    val type: PaymentType
        get() = metadata.type
}