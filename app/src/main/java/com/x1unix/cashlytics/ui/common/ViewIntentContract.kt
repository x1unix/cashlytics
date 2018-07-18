package com.x1unix.cashlytics.ui.common

import android.content.Intent

object ViewIntentContract {
    const val WALLET_ID = "walletId"
    const val BANK_NAME = "bankName"

    fun buildHistoryViewIntent(intent: Intent, walletId: String, bankName: String) {
        intent.putExtra(WALLET_ID, walletId)
        intent.putExtra(BANK_NAME, bankName)
    }
}