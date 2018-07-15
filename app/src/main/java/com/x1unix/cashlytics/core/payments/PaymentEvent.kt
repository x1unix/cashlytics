package com.x1unix.cashlytics.core.payments

import org.joda.time.LocalDateTime

/**
 * Payment event
 *
 * @param date a time when event occured
 * @param metadata payment metadata
 * @param changes balance change
 */
data class PaymentEvent (val bankName: String, val date: LocalDateTime, val metadata: PaymentMetadata, val changes: BalanceChange) {
    val type: PaymentType
        get() = metadata.type
}