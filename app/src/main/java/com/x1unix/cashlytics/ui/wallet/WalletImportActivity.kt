package com.x1unix.cashlytics.ui.wallet

import android.opengl.Visibility
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.x1unix.cashlytics.PermissionHelper
import com.x1unix.cashlytics.R
import com.x1unix.cashlytics.ui.Activity
import com.x1unix.cashlytics.ui.common.PosterViewHolder
import kotlinx.android.synthetic.main.activity_wallet_import.rvProviders
import kotlinx.android.synthetic.main.activity_wallet_import.poster

class WalletImportActivity : Activity() {

    private lateinit var posterViewHolder: PosterViewHolder

    private lateinit var permissionsHelper: PermissionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_import)
        setTitleFromResource(R.string.import_wallet)

        posterViewHolder = PosterViewHolder(poster)
        permissionsHelper = PermissionHelper(baseContext)
        prepareBankList()


        if (permissionsHelper.permissionsGranted) {
            resetPoster()
            getAvailableItems()
        } else {
            showPermissionsRequestMessage()
        }

    }

    private fun showPermissionsRequestMessage() {
        posterViewHolder.setTitle(R.string.sms_permissions_required)
        posterViewHolder.setText(R.string.sms_permissions_description)
        posterViewHolder.setButtonTitle(R.string.provide_permissions)
        posterViewHolder.setIcon(R.drawable.ic_sms_failed)
        posterViewHolder.setActionListener { _ -> permissionsHelper.requirePermissions(this@WalletImportActivity) }
        posterViewHolder.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (permissionsHelper.permissionsGranted) {
            resetPoster()
            getAvailableItems()
        }
    }

    private fun getAvailableItems() {
        try {
            val providers = services.messages.getListOfFoundProviders()

            if (providers.isEmpty()) {
                posterViewHolder.show()
                return
            }

            posterViewHolder.hide()
            rvProviders.visibility = View.VISIBLE

            val adapter = RVWalletListAdapter(providers)
            rvProviders.adapter = adapter

        } catch (ex: Exception) {
            posterViewHolder.setText(ex.message)
            posterViewHolder.setTitle(R.string.cannot_read_sms)
            posterViewHolder.setIcon(R.drawable.ic_sms_failed)
            posterViewHolder.show()
            rvProviders.visibility = View.GONE
        }

    }

    private fun getHelp() {
        // TODO: Redirect to a kb article
    }

    private fun prepareBankList() {
        rvProviders.setHasFixedSize(true)
        val llm = LinearLayoutManager(this)
        rvProviders.layoutManager = llm
    }

    private fun resetPoster() {
        posterViewHolder.setTitle(R.string.no_sms_found)
        posterViewHolder.setText(R.string.wallet_import_no_sms)
        posterViewHolder.setButtonTitle(R.string.more_info)
        posterViewHolder.setIcon(R.drawable.ic_sms)
        posterViewHolder.setActionListener { _ -> getHelp() }
        posterViewHolder.hide()
    }
}
