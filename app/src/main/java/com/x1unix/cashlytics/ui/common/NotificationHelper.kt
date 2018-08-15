package com.x1unix.cashlytics.ui.common

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.x1unix.cashlytics.R
import com.x1unix.cashlytics.core.payments.PaymentEvent
import com.x1unix.cashlytics.core.payments.PaymentType
import com.x1unix.cashlytics.ui.history.WalletHistoryActivity

class NotificationHelper(base: Context): ContextWrapper(base) {
    private var manager: NotificationManager? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)

            chan.enableLights(true)
            chan.enableVibration(true)
            chan.lightColor = Color.GREEN
            chan.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC

            getManager().createNotificationChannel(chan)
        }
    }

    fun getManager(): NotificationManager {
        if (manager == null) {
            manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        return manager!!
    }

    fun buildNotification(c: Context, event: PaymentEvent): Notification {
        val intent = Intent(c, WalletHistoryActivity::class.java)
        ViewIntentContract.buildHistoryViewIntent(intent, event.walletId, event.bankName)

        val contentIntent = PendingIntent.getActivity(c, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        var builder = getBuilder(c)
                .setContentTitle(event.metadata.receiver)
                .setContentText(c.resources.getString(LayoutHelper.getPaymentTypeLabel(event.type)))
                .setSubText(event.changes.charged.toString())
                .setColor(c.resources.getColor(R.color.success))
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val icon = BitmapFactory.decodeResource(c.resources, LayoutHelper.getCardIconResource(event.type))

            builder = builder.setSmallIcon(getEventIcon(event.type))
                    .setLargeIcon(icon)

        } else {
            builder = builder.setSmallIcon(R.mipmap.ic_launcher)
        }

        return builder.build()
    }

    companion object {
        const val CHANNEL_ID = "com.x1unix.cashlytics.ANDROID"
        const val CHANNEL_NAME = "Payment Data"

        fun supportsChannels(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        }

        fun getBuilder(c: Context): NotificationCompat.Builder {
            return if (supportsChannels()) NotificationCompat.Builder(c, CHANNEL_ID) else NotificationCompat.Builder(c)
        }

        fun getEventIcon(e: PaymentType): Int {
            return when (e) {
                PaymentType.Purchase -> R.drawable.ic_cart
                PaymentType.Transfer -> R.drawable.ic_send
                PaymentType.Withdrawal -> R.drawable.ic_wallet
                else -> R.drawable.ic_dollar
            }
        }
    }
}