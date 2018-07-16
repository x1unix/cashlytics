package com.x1unix.cashlytics.ui.wallet

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.View
import com.x1unix.cashlytics.R
import com.x1unix.cashlytics.core.payments.Wallet
import com.x1unix.cashlytics.ui.Activity
import com.x1unix.cashlytics.ui.common.PosterViewHolder
import com.x1unix.cashlytics.ui.common.ViewIntentContract
import com.x1unix.cashlytics.ui.history.WalletHistoryActivity
import com.x1unix.cashlytics.ui.wallet.WalletImportActivity.Companion.UPDATED
import kotlinx.android.synthetic.main.activity_wallets.rvWallets

class WalletsActivity : Activity() {

    private lateinit var posterViewHolder: PosterViewHolder

    private lateinit var poster: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallets)
        setTitleFromResource(R.string.my_wallets)
        prepareView()
        onWalletFetch()
    }

    override fun onResume() {
        Log.d(TAG, "Activity woke up")
        poster = findViewById(R.id.poster)
        posterViewHolder.bind(poster)
        super.onResume()
    }

    private fun prepareView() {
        poster = findViewById(R.id.poster)
        posterViewHolder = PosterViewHolder(poster)
        val llm = LinearLayoutManager(this)
        rvWallets.layoutManager = llm
        resetPoster()
    }

    private fun onWalletFetch() {
        // PosterViewHolder lost all view references after onActivityResult occurs
        // so we should re-mount it again.
        // Keep this workaround until a better solution comes.
        posterViewHolder.bind(poster)

        resetPoster()

        try {
            val wallets = services.wallets.getWallets()

            if (wallets.isEmpty()) {
                posterViewHolder.show()
                return
            }

            posterViewHolder.hide()
            showView(rvWallets)

            val adapter = WalletListAdapter(wallets) {
                onWalletClick(it)
            }

            rvWallets.adapter = adapter

        } catch (ex: Throwable) {
            Log.e(TAG, ex.message)
            hideView(rvWallets)
            posterViewHolder.setTitle(R.string.wallet_fetch_error)
            posterViewHolder.setText(ex.message)
            posterViewHolder.setIcon(R.drawable.ic_error)
            posterViewHolder.displayActionButton(false)
            posterViewHolder.show()
        }
    }

    private fun resetPoster() {
        posterViewHolder.setTitle(R.string.no_items_to_display)
        posterViewHolder.setText(R.string.no_wallets_poster_text)
        posterViewHolder.setButtonTitle(R.string.add_new)
        posterViewHolder.setIcon(R.drawable.ic_wallet)
        posterViewHolder.displayActionButton(true)
        posterViewHolder.setActionListener{ _ -> openImportWizard()}
    }

    private fun openImportWizard() {
        val intent = Intent(this@WalletsActivity, WalletImportActivity::class.java)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) {
            return
        }

        val update = data.getBooleanExtra(UPDATED, false)

        if (!update) {
            return
        }

        Log.i(TAG, "A new wallet has been added, refreshing...")
        onWalletFetch()
    }

    private fun onWalletClick(w: Wallet) {
        val i = Intent(this@WalletsActivity, WalletHistoryActivity::class.java)
        i.putExtra(ViewIntentContract.BANK_NAME, w.bankName)
        i.putExtra(ViewIntentContract.WALLET_ID, w.id)

        startActivity(i)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.wallet_menu, menu)
        return true
    }

    override fun onStop() {
        posterViewHolder.dispose()
        super.onStop()
    }

    companion object {
        const val TAG = "WalletsList"
    }


}
