package com.x1unix.cashlytics.core.storage.repository

import com.couchbase.lite.*
import com.x1unix.cashlytics.core.payments.Wallet
import com.x1unix.cashlytics.core.storage.mappers.WalletMapper

class WalletRepository(db: Database) : Repository<Wallet>(db) {

    override val mapper = WalletMapper()

}