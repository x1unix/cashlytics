package com.x1unix.cashlytics.core.storage.repository

import com.couchbase.lite.Database
import com.couchbase.lite.Expression
import com.x1unix.cashlytics.core.payments.PaymentEvent
import com.x1unix.cashlytics.core.storage.mappers.PaymentEventMapper

class HistoryRepository(db: Database) : Repository<PaymentEvent>(db) {
    override val mapper = PaymentEventMapper()

    /**
     * Gets history timeline for specified wallet
     */
    fun getTimeline(walletId: String): List<PaymentEvent> {
        val results = getQuery()
                .where(Expression.property(PaymentEventMapper.WALLET_ID).equalTo(Expression.string(walletId)))
                .execute()

        return results.allResults().map{ result -> mapper.fromResult(result)}
    }
}