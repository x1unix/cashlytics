package com.x1unix.cashlytics.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.x1unix.cashlytics.R

abstract class Activity : AppCompatActivity() {

    protected val allowGoBack = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallets)
    }

    protected fun setTitleFromResource(resourceId: Int) {
        setTitle(resources.getString(resourceId))
    }

    protected fun setTitle(title: String) {
        if (supportActionBar == null) {
            return
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(allowGoBack)
        supportActionBar!!.title = title
        supportActionBar!!.show()
    }
}
