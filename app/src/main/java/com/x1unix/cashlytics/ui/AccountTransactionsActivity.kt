package com.x1unix.cashlytics.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.x1unix.cashlytics.R
import kotlinx.android.synthetic.main.activity_account_transactions.rvTransactions

class AccountTransactionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_transactions)
    }
}
