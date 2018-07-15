package com.x1unix.cashlytics.core.payments

import com.couchbase.lite.Dictionary
import com.couchbase.lite.MutableDictionary

data class Amount(val amount: Double, val currency: String) {
    override fun toString(): String {
        return amount.toString() + " " + currency
    }

    fun toDictionary(): MutableDictionary {
        return MutableDictionary()
                .setDouble(AMOUNT, amount)
                .setString(CURRENCY, currency)
    }

    fun isNegative(): Boolean {
        return amount < 0
    }

    companion object {
        const val AMOUNT = "amount"

        const val CURRENCY = "currency"

        fun fromDictionary(d: Dictionary): Amount {
            val amount = d.getDouble(AMOUNT)
            val currency = d.getString(CURRENCY)

            return Amount(amount, currency)
        }

        fun fromString(str: String): Amount {
            val chunks = str.split(' ')
            return Amount(chunks[0].toDouble(), chunks[1])
        }

        fun empty(): Amount {
            return Amount(0.0, "USD")
        }
    }
}