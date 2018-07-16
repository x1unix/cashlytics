package com.x1unix.cashlytics.core.storage.repository

import android.util.Log
import com.couchbase.lite.*
import com.x1unix.cashlytics.core.payments.PaymentEvent
import com.x1unix.cashlytics.core.storage.mappers.Mapper
import com.x1unix.cashlytics.core.storage.mappers.PaymentEventMapper

class HistoryRepository(db: Database) : Repository<PaymentEvent>(db) {
    override val mapper = PaymentEventMapper()

    /**
     * Gets history timeline for specified wallet
     */
    fun getTimeline(walletId: String): List<PaymentEvent> {
        val query = getQuery(
                wheres = Expression.property(property(PaymentEventMapper.WALLET_ID)).equalTo(Expression.string(walletId))
        ).orderBy(Ordering.property(property(PaymentEventMapper.DATE)).descending())

        Log.d("History", "Query: '${query.explain()}'")

        val results = query.execute()

        return results.allResults().map { result -> mapper.fromResult(result) }
    }
}