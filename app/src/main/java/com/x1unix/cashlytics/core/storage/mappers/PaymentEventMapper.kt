package com.x1unix.cashlytics.core.storage.mappers

import com.couchbase.lite.Dictionary
import com.couchbase.lite.MutableDictionary
import com.x1unix.cashlytics.core.payments.BalanceChange
import com.x1unix.cashlytics.core.payments.PaymentEvent
import com.x1unix.cashlytics.core.payments.PaymentMetadata
import org.joda.time.LocalDateTime
import java.util.*

class PaymentEventMapper : Mapper<PaymentEvent>() {
    override val objectType = "paymentEvent"

    override fun wrap(item: PaymentEvent): Dictionary {
        return MutableDictionary()
                .setString(BANK_NAME, item.bankName)
                .setDate(DATE, item.date.toDate())
                .setDictionary(METADATA, item.metadata.toDictionary())
                .setDictionary(CHANGES, item.changes.toDictionary())
                .setString(WALLET_ID, item.walletId)
    }

    override fun unwrap(dict: Dictionary, itemId: String): PaymentEvent {
        val bankName = dict.getString(BANK_NAME)
        val walletId = dict.getString(WALLET_ID)
        val date = LocalDateTime(dict.getDate(DATE))
        val metadata = PaymentMetadata.fromDictionary(dict.getDictionary(METADATA))
        val changes = BalanceChange.fromDictionary(dict.getDictionary(CHANGES))

        return PaymentEvent(
                bankName,
                date,
                metadata,
                changes,
                itemId,
                walletId
        )
    }

    companion object {
        const val BANK_NAME = "bankName"
        const val DATE = "date"
        const val METADATA = "metadata"
        const val CHANGES = "changes"
        const val WALLET_ID = "walletId"

    }
}