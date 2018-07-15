package com.x1unix.cashlytics.core.storage

import android.content.Context
import com.couchbase.lite.Database
import com.couchbase.lite.DatabaseConfiguration
import com.x1unix.cashlytics.core.storage.repository.HistoryRepository
import com.x1unix.cashlytics.core.storage.repository.WalletRepository

/**
 * Initializes database storage and provides access to several repositories.
 *
 * @param context Android application context
 * @param databaseName Database name
 */
class Storage(context: Context, databaseName: String) {

    /**
     * Couchbase database instance
     */
    private val database = Database(databaseName, DatabaseConfiguration(context))

    /**
     * Payments history repository
     */
    val history = HistoryRepository(database)

    /**
     * Wallets repository
     */
    val wallet = WalletRepository(database)

    companion object {
        /**
         * Default database name
         */
        private const val DB_NAME = "userdata"

        /**
         * Creates a new storage holder with default settings
         */
        fun withDefaults(context: Context): Storage {
            return Storage(context, DB_NAME)
        }
    }
}