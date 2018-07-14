package com.x1unix.cashlytics

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import com.x1unix.cashlytics.core.Cashlytics
import android.widget.Toast
import butterknife.BindView
import com.x1unix.cashlytics.ui.history.AccountTransactionsActivity
import com.x1unix.cashlytics.ui.history.BANK_NAME


class MainActivity : AppCompatActivity() {
    private lateinit var permissions: PermissionHelper

//    @BindView(R.id.providersList) lateinit var providersList: ListView
    lateinit var providersList: LinearLayout

    private val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateActionBar()
        bindViews()
        permissions = PermissionHelper(baseContext)

        if (permissions.permissionsGranted) {
            readMessages()
        } else {
            permissions.requirePermissions(this)
        }
    }

    @Override
    protected fun updateActionBar() {
        supportActionBar!!.title = getString(R.string.pick_bank_name)
        supportActionBar!!.show()
    }

    private fun bindViews() {
        providersList = findViewById(R.id.providersList)
    }

    fun readMessages() {
        val providers = Cashlytics.messages.getListOfFoundProviders()
        for (provider in providers) {
            val b = Button(this)
            b.tag = provider
            b.text = provider
            b.setOnClickListener(providerPick)
            providersList.addView(b)
        }
    }

    var providerPick: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(v: View) {
            val tag = v.tag
            Toast.makeText(applicationContext, "Picked $tag", Toast.LENGTH_SHORT).show()

            val intent = Intent(context, AccountTransactionsActivity::class.java).apply {
                putExtra(BANK_NAME, tag.toString())
            }

            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        readMessages()
    }
}
