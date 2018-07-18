package com.x1unix.cashlytics.ui.common

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import com.x1unix.cashlytics.R
import com.x1unix.cashlytics.core.payments.PaymentEvent
import com.x1unix.cashlytics.core.payments.PaymentType
import com.x1unix.cashlytics.ui.history.WalletHistoryActivity

object LayoutHelper {
    fun getEventIcon(e: PaymentType): Int {
        return when (e) {
            PaymentType.Internet -> R.drawable.ic_globe
            PaymentType.Purchase -> R.drawable.ic_cart
            PaymentType.Transfer -> R.drawable.ic_send
            PaymentType.Withdrawal -> R.drawable.ic_up
            else -> R.drawable.ic_dollar
        }
    }

    fun getPaymentTypeLabel(pt: PaymentType): Int {
        return when (pt) {
            PaymentType.Internet -> R.string.pt_internet
            PaymentType.Purchase -> R.string.pt_purchase
            PaymentType.Transfer -> R.string.pt_transfer
            PaymentType.Withdrawal -> R.string.pt_withdrawal
            PaymentType.Refill -> R.string.pt_refill
            PaymentType.Debit -> R.string.pt_debit
            else -> R.string.pt_unknown
        }
    }

    fun getCardIconResource(pt: PaymentType): Int {
        return when (pt) {
            PaymentType.Transfer -> R.drawable.ic_pa_transfer
            PaymentType.Internet -> R.drawable.ic_pa_web
            PaymentType.Purchase -> R.drawable.ic_pa_purchase
            PaymentType.Withdrawal -> R.drawable.ic_pa_withdrawal
            else -> R.drawable.ic_pa_common
        }
    }
}