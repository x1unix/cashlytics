package com.x1unix.cashlytics.background.listeners

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import android.os.Build
import android.os.Bundle
import com.x1unix.cashlytics.Application
import com.x1unix.cashlytics.ui.common.LayoutHelper
import org.jetbrains.anko.doAsync


class MessageListener: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val application = context.applicationContext as Application
        val handlers = application.serviceContainer.messageProcessor
        val wallets = application.serviceContainer.wallets

        val myWallets = wallets.getAll().map { it.bankName to it }.toMap()

        if (intent.action != ACTION) {
            return
        }

        val bundle = intent.extras

        if (bundle == null) {
            return
        }

        Log.i(TAG, "A new message received")

        val nm = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        try {
            val pdus = bundle.get("pdus") as Array<Any>

            for (aObject in pdus) {
                val sms = getIncomingMessage(aObject, bundle)
                val sender = sms.displayOriginatingAddress

                if (!myWallets.containsKey(sender) || !handlers.hasHandlerFor(sender)) {
                    continue
                }

                val contents = sms.displayMessageBody
                val event = handlers.getProviderProcessor(sender).parseMessage(contents)
                val wallet = myWallets[sender]!!

                event.walletId = wallet.id

                doAsync (exceptionHandler = {e: Throwable -> onError(e)}){
                    wallets.addWalletEvent(myWallets[sender]!!, event)
                }

                val nofitication = LayoutHelper.buildNotification(context, event)
                nm.notify(1, nofitication)
            }

            this.abortBroadcast()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to inspect received message(s): ${e.message}")
        }
    }

    private fun onError(e: Throwable) {
        Log.e(TAG, "Failed to update wallet status (${e.message})")
    }

    private fun getIncomingMessage(aObject: Any, bundle: Bundle): SmsMessage {
        val currentSMS: SmsMessage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val format = bundle.getString("format")
            currentSMS = SmsMessage.createFromPdu(aObject as ByteArray, format)
        } else {
            currentSMS = SmsMessage.createFromPdu(aObject as ByteArray)
        }
        return currentSMS
    }

    companion object {
        private const val TAG = "MessageListener"
        private const val ACTION = "android.provider.Telephony.SMS_RECEIVED"
    }
}