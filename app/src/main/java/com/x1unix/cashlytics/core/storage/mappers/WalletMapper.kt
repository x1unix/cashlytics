package com.x1unix.cashlytics.core.storage.mappers

import com.couchbase.lite.Dictionary
import com.couchbase.lite.MutableDictionary
import com.x1unix.cashlytics.core.payments.Amount
import com.x1unix.cashlytics.core.payments.Wallet
import org.joda.time.LocalDateTime

class WalletMapper : Mapper<Wallet>() {
    override val objectType = "wallet"

    override fun wrap(item: Wallet): Dictionary {
        return MutableDictionary()
                .setString(BANK_NAME, item.bankName)
                .setString(DESCRIPTION, item.description)
                .setString(STATUS, item.status.toString())
                .setDate(DATE, item.lastUpdated.toDate())
    }

    override fun unwrap(dict: Dictionary, itemId: String): Wallet {
        return Wallet(
                bankName = dict.getString(BANK_NAME),
                status = Amount.fromString(dict.getString(STATUS)),
                lastUpdated = LocalDateTime(dict.getDate(DATE)),
                description = dict.getString(DESCRIPTION),
                id = itemId
        )
    }

    companion object {
        const val BANK_NAME = "bankName"
        const val DESCRIPTION = "description"
        const val STATUS = "status"
        const val DATE = "date"
    }
}