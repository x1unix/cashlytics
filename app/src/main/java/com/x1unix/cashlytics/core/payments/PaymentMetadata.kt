package com.x1unix.cashlytics.core.payments

import com.couchbase.lite.Dictionary
import com.couchbase.lite.MutableDictionary


data class PaymentMetadata (val type: PaymentType, val receiver: String?) {
    fun toDictionary(): Dictionary {
        return MutableDictionary()
                .setString(TYPE, type.value)
                .setString(RECEIVER, receiver ?: "nobody")
    }

    companion object {
        const val TYPE = "type"
        const val RECEIVER = "receiver"

        fun fromDictionary(d: Dictionary): PaymentMetadata {
            var type: PaymentType

            try {
                type = PaymentType.valueOf(d.getString(TYPE))
            } catch (ex: IllegalArgumentException) {
                type = PaymentType.Unknown
            }

            val receiver = d.getString(RECEIVER)

            return PaymentMetadata(type, receiver)
        }
    }
}