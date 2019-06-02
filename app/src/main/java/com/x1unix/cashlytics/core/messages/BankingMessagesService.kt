package com.x1unix.cashlytics.core.messages

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.Telephony
import android.util.Log
import com.x1unix.cashlytics.core.payments.PaymentEvent
import com.x1unix.cashlytics.core.payments.Wallet
import com.x1unix.cashlytics.core.providers.MessageProcessor
import com.x1unix.cashlytics.core.providers.internals.MessageProcessorConsumer
import java.lang.Exception
import javax.inject.Inject

const val COLUMN_THREAD_ID = "thread_id"
const val COLUMN_BODY = "body"
const val COLUMN_CREATOR = "creator"
val projections = arrayOf("_id",
        "date",
        "address",
        "from_address",
        "body")

/**
 * Provides access to data from SMS messages, such as list of bank conversations and operations.
 *
 * @param context Base application context
 * @param messageProcessor Message processor service instance
 */
class BankingMessagesService(var context: Context, var messageProcessor: MessageProcessor) {

    /**
     * Gets actions history for specified bank account from SMS conversation
     *
     * @param providerName SMS sender name (bank name)
     */
    fun getProviderHistory(providerName: String): List<PaymentEvent> {
        val processor = messageProcessor.getProviderProcessor(providerName)
        val conversations = getProviderMessages(providerName)
        val events = arrayListOf<PaymentEvent>()
        for(c in conversations) {
            try {
                val event = processor.parseMessage(c.contents)
                events.add(event)
            } catch (ex: Exception) {
                Log.e("Cashlytics", ex.message)
            }
        }

        return events
    }

    /**
     * Gets all SMS messages from bank SMS conversation sorted by date
     *
     * @param providerName Bank name
     */
    fun getProviderMessages(providerName: String): MutableSet<Message> {
        val messagesQuery = Uri.parse("content://sms/inbox")
        val cursor = context.contentResolver.query(
                messagesQuery,
                null,
                "address like '%$providerName%'",
                null,
                "${Telephony.TextBasedSmsColumns.DATE} DESC"
        )

        return constructMessages(cursor)
    }

    val smsQuery: String
        get() = if (Build.MANUFACTURER == "samsung") "content://mms-sms/complete-conversations" else "content://mms-sms/conversations/"

    val smsCols: Array<String>
            get() = if (Build.MANUFACTURER == "samsung") projections else arrayOf("*")

    /**
     * Gets list of all found and recognised bank conversations
     *
     * @return Wallet created from bank conversation data
     */
    fun getListOfFoundProviders(): List<Wallet> {
        val uri = Uri.parse(smsQuery)
        val cursor = context.contentResolver.query(uri, smsCols, null, null, null)

        val conversations = constructMessages(cursor).distinctBy { message -> message.sender }
        val messages = arrayListOf<Wallet>();
        for(c in conversations) {
            if (!messageProcessor.hasHandlerFor(c.sender)) {
                continue
            }

            try {
                val provider = messageProcessor.getProviderProcessor(c.sender)
                val w = provider.buildWallet(provider.parseMessage(c.contents), this@BankingMessagesService)
                        .setBrandIconResource(provider.brandIcon)
                messages.add(w);
            } catch (ex: Exception) {
                Log.e("Cashlytics", ex.message)
            }
        }

        return messages;
    }

    /**
     * Maps SMS metadata into message object
     */
    private fun constructMessages(cursor: Cursor): MutableSet<Message> {
        val result = mutableSetOf<Message>()
        if (cursor.moveToFirst()) {
            do {
                val body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.TextBasedSmsColumns.BODY))
                val address = cursor.getString(cursor.getColumnIndex("address"))
                val msg = Message(address, body)
                result.add(msg)
            } while (cursor.moveToNext())
        }

        return result
    }
}