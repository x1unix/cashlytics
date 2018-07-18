package com.x1unix.cashlytics.core.storage.repository

import android.util.Log
import com.couchbase.lite.Document
import com.couchbase.lite.Expression
import com.couchbase.lite.Database
import com.x1unix.cashlytics.core.payments.Amount
import com.x1unix.cashlytics.core.payments.PaymentEvent
import com.x1unix.cashlytics.core.payments.Wallet
import com.x1unix.cashlytics.core.storage.mappers.WalletMapper
import org.joda.time.LocalDateTime

class WalletRepository(db: Database) : Repository<Wallet>(db) {

    override val mapper = WalletMapper()

    /**
     * Finds a wallet by bank name
     *
     * @param name Bank name
     */
    fun findByName(name: String): Wallet {
        val results = getQuery(
                wheres = Expression.property(property(WalletMapper.BANK_NAME)).equalTo(Expression.string(name))
        ).execute()

        return results.allResults().map { result -> mapper.fromResult(result) }.first()
    }

    /**
     * Updates wallet status
     */
    fun updateWalletStatus(walletId: String, lastModifiedTime: LocalDateTime, amount: Amount) {
        val document = database.getDocument(walletId).toMutable()

        mapper.applyUpdate(document) {
            it.setDate(WalletMapper.DATE, lastModifiedTime.toDate())
                    .setString(WalletMapper.STATUS, amount.toString())
        }

        Log.d("Wallet", "Wallet $walletId updated (newAmount: $amount)")
        database.save(document)
    }

}