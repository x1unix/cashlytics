package com.x1unix.cashlytics.core.payments

import com.couchbase.lite.Dictionary
import com.couchbase.lite.MutableDictionary

/**
 * Balance change record
 *
 * @param charged Charged amount
 * @param left Balance without overdraft
 * @param overdraft Overdraft
 * @param available Total available funds including overdraft
 */
data class BalanceChange(
        var charged: Amount? = null,
        var left: Amount? = null,
        var overdraft: Amount? = null,
        var available: Amount? = null
) {
    fun toDictionary(): Dictionary {
        return MutableDictionary()
                .setDictionary(CHARGED, (charged ?: Amount.empty()).toDictionary())
                .setDictionary(LEFT, (left ?: Amount.empty()).toDictionary())
                .setDictionary(OVERDRAFT, (overdraft ?: Amount.empty()).toDictionary())
                .setDictionary(AVAILABLE, (available ?: Amount.empty()).toDictionary())
    }

    companion object {
        const val CHARGED = "charged"
        const val LEFT = "left"
        const val OVERDRAFT = "left"
        const val AVAILABLE = "available"

        fun fromDictionary(d: Dictionary): BalanceChange {
            val charged = Amount.fromDictionary(d.getDictionary(CHARGED))
            val left = Amount.fromDictionary(d.getDictionary(LEFT))
            val overdraft = Amount.fromDictionary(d.getDictionary(OVERDRAFT))
            val available = Amount.fromDictionary(d.getDictionary(AVAILABLE))

            return BalanceChange(charged, left, overdraft, available)
        }
    }
}