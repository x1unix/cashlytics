package com.x1unix.cashlytics.core.messages

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.Telephony
import com.x1unix.cashlytics.core.payments.PaymentEvent
import com.x1unix.cashlytics.core.payments.Wallet
import com.x1unix.cashlytics.core.providers.MessageProcessor
import com.x1unix.cashlytics.core.providers.internals.MessageProcessorConsumer
import javax.inject.Inject

const val COLUMN_THREAD_ID = "thread_id"
const val COLUMN_BODY = "body"
const val COLUMN_CREATOR = "creator"

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
        return getProviderMessages(providerName)
                .map{i: Message -> processor.parseMessage(i.contents)}
                .sortedWith(compareBy{it.date})
    }

    /**
     * Gets all SMS messages from bank SMS conversation
     *
     * @param providerName Bank name
     */
    fun getProviderMessages(providerName: String): MutableSet<Message> {
        val messagesQuery = Uri.parse("content://sms/inbox")
        val cursor = context.contentResolver.query(messagesQuery, null, "address like '%$providerName%'", null, null)

        return constructMessages(cursor)
    }

    /**
     * Gets list of all found and recognised bank conversations
     *
     * @return Wallet created from bank conversation data
     */
    fun getListOfFoundProviders(): List<Wallet> {
        val projection = arrayOf("*")
        val uri = Uri.parse("content://mms-sms/conversations/")
        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        val conversations = constructMessages(cursor)

        return conversations.filter {
            message: Message -> messageProcessor.hasHandlerFor(message.sender)
        }.map (fun (m: Message): Wallet {
            val provider = messageProcessor.getProviderProcessor(m.sender)
            return Wallet.fromEvent(provider.parseMessage(m.contents))
                    .setBrandIconResource(provider.brandIcon)
        })
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