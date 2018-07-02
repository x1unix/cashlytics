package com.x1unix.cashlytics.payments

import org.joda.time.LocalDateTime

/**
 * Payment event
 *
 * @param date a time when event occured
 * @param metadata payment metadata
 * @param charged amount of credits charged
 * @param balance balance left on account
 * @param overdraft amount of credit funds available
 */
data class PaymentEvent (var date: LocalDateTime, var metadata: PaymentMetadata, var charged: Int = 0, var balance: Int = 0, var overdraft: Int = 0) {
    val type: PaymentType
        get() = metadata.type
}