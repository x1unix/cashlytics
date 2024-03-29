package com.x1unix.cashlytics.ui.wallet

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.x1unix.cashlytics.BuildConfig
import com.x1unix.cashlytics.R
import com.x1unix.cashlytics.core.payments.Wallet
import com.x1unix.cashlytics.ui.Activity
import com.x1unix.cashlytics.ui.DebugActivity
import com.x1unix.cashlytics.ui.common.PosterViewHolder
import com.x1unix.cashlytics.ui.common.ViewIntentContract
import com.x1unix.cashlytics.ui.history.WalletHistoryActivity
import com.x1unix.cashlytics.ui.wallet.WalletImportActivity.Companion.UPDATED
import kotlinx.android.synthetic.main.activity_wallets.rvWallets
import kotlinx.android.synthetic.main.activity_wallets.addWalletButton
import kotlinx.android.synthetic.main.activity_wallets.swipeRefresh

class WalletsActivity : Activity(), SwipeRefreshLayout.OnRefreshListener {

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
        addWalletButton.setOnClickListener{ _ -> openImportWizard()}
        poster = findViewById(R.id.poster)
        posterViewHolder = PosterViewHolder(poster)
        val llm = LinearLayoutManager(this)
        rvWallets.layoutManager = llm
        resetPoster()
        swipeRefresh.setOnRefreshListener(this)
        swipeRefresh.setColorSchemeColors(resources.getColor(R.color.gold))
    }

    override fun onRefresh() {
        swipeRefresh.isRefreshing = true
        onWalletFetch()
        swipeRefresh.isRefreshing = false
    }

    private fun onWalletFetch() {
        // PosterViewHolder lost all view references after onActivityResult occurs
        // so we should re-mount it again.
        // Keep this workaround until a better solution comes.
        posterViewHolder.bind(poster)

        resetPoster()
        hideView(addWalletButton)

        try {
            val wallets = services.wallets.getWallets()

            if (wallets.isEmpty()) {
                posterViewHolder.show()
                return
            }

            posterViewHolder.hide()
            showView(swipeRefresh)
            showView(addWalletButton)

            val adapter = WalletListAdapter(wallets) {
                onWalletClick(it)
            }

            rvWallets.adapter = adapter

        } catch (ex: Throwable) {
            Log.e(TAG, ex.message)
            hideView(swipeRefresh)
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.action_debug -> onDebug()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun onDebug() {
        val intent = Intent(this@WalletsActivity, DebugActivity::class.java)
        startActivity(intent)
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
        if (BuildConfig.DEBUG) {
            menuInflater.inflate(R.menu.wallet_menu, menu)
            return true
        }
        return false
    }

    override fun onStop() {
        posterViewHolder.dispose()
        super.onStop()
    }

    companion object {
        const val TAG = "WalletsList"
    }


}
