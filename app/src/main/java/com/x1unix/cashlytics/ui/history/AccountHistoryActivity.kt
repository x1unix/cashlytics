package com.x1unix.cashlytics.ui.history

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.x1unix.cashlytics.R
import com.x1unix.cashlytics.ui.Activity
import kotlinx.android.synthetic.main.activity_account_transactions.rvTransactions

const val BANK_NAME = "bankName"

class AccountTransactionsActivity : Activity() {

    private lateinit var bankName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_transactions)

        if (intent != null && intent.hasExtra(BANK_NAME)) {
            bankName = intent.getStringExtra(BANK_NAME)
            initializeTransactionsView()
            updateActionBar()
            showProviderMessages(bankName)
        }
    }

    private fun updateActionBar() {
        supportActionBar!!.title = bankName
        supportActionBar!!.show()
    }

    private fun initializeTransactionsView() {
        rvTransactions.setHasFixedSize(true)
        val llm = LinearLayoutManager(this)
        rvTransactions.layoutManager = llm
    }

    private fun showProviderMessages(providerName: String) {
        val events = services.messages.getProviderHistory(providerName)
        val adapter = RVHistoryFeedAdapter(events)
        rvTransactions.adapter = adapter
    }
}
