package com.x1unix.cashlytics.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.x1unix.cashlytics.R
import com.x1unix.cashlytics.ui.common.PosterViewHolder
import com.x1unix.cashlytics.ui.history.AccountTransactionsActivity
import com.x1unix.cashlytics.ui.history.BANK_NAME
import kotlinx.android.synthetic.main.activity_wallets.rvWallets
import kotlinx.android.synthetic.main.activity_wallets.poster

class WalletsActivity : Activity() {

    private lateinit var posterViewHolder: PosterViewHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallets)
        setTitleFromResource(R.string.my_wallets)
    }

    override fun onStart() {
        preparePoster()
        posterViewHolder.show()
        super.onStart()
    }

    private fun preparePoster() {
        posterViewHolder = PosterViewHolder(poster)
        posterViewHolder.setTitle(R.string.no_items_to_display)
        posterViewHolder.setText(R.string.no_wallets_poster_text)
        posterViewHolder.setButtonTitle(R.string.add_new)
        posterViewHolder.setIcon(R.drawable.ic_wallet)
        posterViewHolder.setActionListener{ _ -> importWallet()}
    }

    fun importWallet() {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.wallet_menu, menu)
        return true
    }

    override fun onStop() {
        posterViewHolder.dispose()
        super.onStop()
    }


}
