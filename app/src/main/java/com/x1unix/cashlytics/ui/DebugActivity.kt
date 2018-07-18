package com.x1unix.cashlytics.ui

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.x1unix.cashlytics.R
import com.x1unix.cashlytics.core.payments.*
import com.x1unix.cashlytics.ui.common.LayoutHelper
import com.x1unix.cashlytics.ui.common.NotificationHelper
import kotlinx.android.synthetic.main.activity_debug.show_notification_btn
import org.joda.time.LocalDateTime

class DebugActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)
        setTitle("Debug Menu", true)

        show_notification_btn.setOnClickListener{ _ -> testNotification()}
    }

    fun testNotification() {
        val event = PaymentEvent(
                "SomeBank",
                LocalDateTime(),
                PaymentMetadata(PaymentType.Purchase, "SomeShop"),
                BalanceChange(
                        Amount(100.24, "UAH"),
                        Amount(2045.12, "UAH"),
                        Amount.empty(),
                        Amount.empty()
                ),
                "null",
                "null"
        )

        val helper = NotificationHelper(this)
        val notification = helper.buildNotification(this, event)
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        Log.d(TAG, "Notification test sent")

        nm.notify(1, notification)
    }

    companion object {
        const val TAG = "Debug"
    }
}
