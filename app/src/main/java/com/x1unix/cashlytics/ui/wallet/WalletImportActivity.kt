package com.x1unix.cashlytics.ui.wallet

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.x1unix.cashlytics.R
import com.x1unix.cashlytics.core.Cashlytics
import com.x1unix.cashlytics.ui.Activity
import com.x1unix.cashlytics.ui.common.PosterViewHolder
import kotlinx.android.synthetic.main.activity_account_transactions.*
import kotlinx.android.synthetic.main.activity_wallet_import.rvProviders
import kotlinx.android.synthetic.main.activity_wallet_import.poster

class WalletImportActivity : Activity() {

    private lateinit var posterViewHolder: PosterViewHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_import)
        setTitleFromResource(R.string.import_wallet)
        preparePoster()
        prepareBankList()
        getAvailableItems()
    }

    private fun getAvailableItems() {
        try {
            val providers = Cashlytics.messages.getListOfFoundProviders()

            if (providers.isEmpty()) {
                posterViewHolder.show()
                return
            }

            val adapter = RVWalletListAdapter(providers)
            rvProviders.adapter = adapter

        } catch (ex: Exception) {
            posterViewHolder.setText(ex.message)
            posterViewHolder.show()
        }

    }

    private fun prepareBankList() {
        rvProviders.setHasFixedSize(true)
        val llm = LinearLayoutManager(this)
        rvTransactions.layoutManager = llm
    }

    private fun preparePoster() {
        posterViewHolder = PosterViewHolder(poster)
        posterViewHolder.setTitle(R.string.no_sms_found)
        posterViewHolder.setText(R.string.wallet_import_no_sms)
        posterViewHolder.setButtonTitle(R.string.more_info)
        posterViewHolder.setIcon(R.drawable.ic_sms)
    }
}
