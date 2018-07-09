package com.x1unix.cashlytics.core.messages

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.Telephony
import com.x1unix.cashlytics.core.providers.MessageProcessor
import com.x1unix.cashlytics.core.providers.internals.MessageProcessorConsumer
import javax.inject.Inject

const val COLUMN_THREAD_ID = "thread_id"
const val COLUMN_BODY = "body"
const val COLUMN_CREATOR = "creator"

class BankingMessagesService(var context: Context, var messageProcessor: MessageProcessor) {
    fun getAllMessages(): MutableSet<Message> {

        val messagesQuery = Uri.parse("content://sms/inbox")
        val cursor = context.contentResolver.query(messagesQuery, null, null, null, null)

        return constructMessages(cursor)
    }

    fun getListOfFoundProviders(): List<String> {
        val projection = arrayOf("*")
        val uri = Uri.parse("content://mms-sms/conversations/")
        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        val conversations = constructMessages(cursor)

        return conversations.map(fun (i: Message): String {
            return i.sender
        }).filter(fun (provider: String) : Boolean {
            return messageProcessor.hasHandlerFor(provider)
        })
    }

    private fun constructMessages(cursor: Cursor): MutableSet<Message> {
        val result = mutableSetOf<Message>();
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