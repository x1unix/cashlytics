package com.x1unix.cashlytics.core.storage.mappers

import com.couchbase.lite.Document
import com.couchbase.lite.MutableDocument
import com.x1unix.cashlytics.core.payments.BalanceChange
import com.x1unix.cashlytics.core.payments.PaymentEvent
import com.x1unix.cashlytics.core.payments.PaymentMetadata
import com.x1unix.cashlytics.core.storage.Mapper
import org.joda.time.LocalDateTime

class PaymentEventMapper : Mapper<PaymentEvent> {
    override fun toDocument(item: PaymentEvent): MutableDocument {
        return MutableDocument()
                .setString(BANK_NAME, item.bankName)
                .setDate(DATE, item.date.toDate())
                .setDictionary(METADATA, item.metadata.toDictionary())
                .setDictionary(CHANGES, item.changes.toDictionary())
                .setString(WALLET_ID, item.walletId)
    }

    override fun fromDocument(doc: Document): PaymentEvent {
        val bankName = doc.getString(BANK_NAME)
        val walletId = doc.getString(WALLET_ID)
        val id = doc.id
        val date = LocalDateTime(doc.getDate(DATE))
        val metadata = PaymentMetadata.fromDictionary(doc.getDictionary(METADATA))
        val changes = BalanceChange.fromDictionary(doc.getDictionary(CHANGES))

        return PaymentEvent(
                bankName,
                date,
                metadata,
                changes,
                id,
                walletId
        )
    }

    companion object {
        const val BANK_NAME = "bankName"
        const val DATE = "date"
        const val METADATA = "METADATA"
        const val CHANGES = "changes"
        const val WALLET_ID = "walletId"

    }
}