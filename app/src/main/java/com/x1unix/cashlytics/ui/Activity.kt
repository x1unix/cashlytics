package com.x1unix.cashlytics.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.x1unix.cashlytics.Application
import com.x1unix.cashlytics.R
import com.x1unix.cashlytics.core.ServiceContainer

abstract class Activity : AppCompatActivity() {
    /**
     * Gets Cashlytics service container
     */
    protected val services: ServiceContainer
        get() = (application as Application).serviceContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallets)
    }

    protected fun setTitleFromResource(resourceId: Int) {
        setTitle(resources.getString(resourceId))
    }

    protected fun setTitle(title: String, allowGoBack: Boolean = false) {
        if (supportActionBar == null) {
            return
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(allowGoBack)
        supportActionBar!!.title = title
        supportActionBar!!.show()
    }

    protected fun hideView(w: View) {
        w.visibility = View.GONE
    }

    protected fun showView(w: View) {
        w.visibility = View.VISIBLE
    }
}
