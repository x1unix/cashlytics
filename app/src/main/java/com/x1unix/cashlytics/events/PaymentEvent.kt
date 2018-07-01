package com.x1unix.cashlytics.events

import org.joda.time.LocalDateTime

/**
 * Payment event
 */
data class PaymentEvent (val date: LocalDateTime, val type: PaymentType, val charged: Int, val balance: Int, val overdraft: Int);