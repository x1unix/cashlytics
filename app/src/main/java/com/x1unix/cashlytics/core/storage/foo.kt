package com.x1unix.cashlytics.core.storage

import com.couchbase.lite.Document
import com.couchbase.lite.MutableDocument
import com.x1unix.cashlytics.core.payments.Wallet

class WalletMapper: Mapper<Wallet> {
    override fun toDocument(item: Wallet): MutableDocument {
        val document = MutableDocument()
                .setString(BANK_NAME, item.bankName)
                .setString(DESCRIPTION, item.description)

    }

    companion object {
        const val BANK_NAME = "bankName"
        const val DESCRIPTION = "description"
    }
}