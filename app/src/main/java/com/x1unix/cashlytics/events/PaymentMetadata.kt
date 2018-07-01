package com.x1unix.cashlytics.events

data class PaymentMetadata (val type: PaymentType, val receiver: String?);