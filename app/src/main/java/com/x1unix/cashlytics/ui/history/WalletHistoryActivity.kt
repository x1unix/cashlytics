package com.x1unix.cashlytics.ui.history

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.x1unix.cashlytics.R
import com.x1unix.cashlytics.ui.Activity
import com.x1unix.cashlytics.ui.common.ViewIntentContract
import kotlinx.android.synthetic.main.activity_account_transactions.rvTransactions

class WalletHistoryActivity : Activity() {

    private lateinit var bankName: String

    private lateinit var walletId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_transactions)

        if (intent != null && intent.hasExtra(ViewIntentContract.BANK_NAME)) {
            bankName = intent.getStringExtra(ViewIntentContract.BANK_NAME)
            walletId = intent.getStringExtra(ViewIntentContract.WALLET_ID)

            initializeTransactionsView()
            setTitle(bankName, true)
            loadWalletHistory(walletId)
        }
    }

    private fun initializeTransactionsView() {
        rvTransactions.setHasFixedSize(true)
        val llm = LinearLayoutManager(this)
        rvTransactions.layoutManager = llm
    }

    private fun loadWalletHistory(walletId: String) {
        val events = services.history.getWalletTimeline(walletId)
        val adapter = RVHistoryFeedAdapter(events)
        rvTransactions.adapter = adapter
    }
}
