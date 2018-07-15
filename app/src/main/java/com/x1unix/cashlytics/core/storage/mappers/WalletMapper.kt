package com.x1unix.cashlytics.core.storage.mappers

import com.couchbase.lite.Document
import com.couchbase.lite.MutableDocument
import com.x1unix.cashlytics.core.payments.Amount
import com.x1unix.cashlytics.core.payments.Wallet
import com.x1unix.cashlytics.core.storage.Mapper
import org.joda.time.LocalDateTime

class WalletMapper : Mapper<Wallet> {
    override fun toDocument(item: Wallet): MutableDocument {
        return MutableDocument()
                .setString(BANK_NAME, item.bankName)
                .setString(DESCRIPTION, item.description)
                .setString(STATUS, item.status.toString())
                .setDate(DATE, item.lastUpdated.toDate())
    }

    override fun fromDocument(doc: Document): Wallet {
        return Wallet(
                bankName = doc.getString(BANK_NAME),
                status = Amount.fromString(doc.getString(STATUS)),
                lastUpdated = LocalDateTime(doc.getDate(DATE)),
                description = doc.getString(DESCRIPTION),
                id = doc.id
        )
    }

    companion object {
        const val BANK_NAME = "bankName"
        const val DESCRIPTION = "description"
        const val STATUS = "status"
        const val DATE = "date"
    }
}