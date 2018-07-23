package com.x1unix.cashlytics.core.providers.kredobank

import com.x1unix.cashlytics.R
import com.x1unix.cashlytics.core.messages.BankingMessagesService
import com.x1unix.cashlytics.core.payments.Amount
import com.x1unix.cashlytics.core.payments.PaymentEvent
import com.x1unix.cashlytics.core.payments.PaymentType
import com.x1unix.cashlytics.core.payments.Wallet
import com.x1unix.cashlytics.core.providers.MessageParser
import com.x1unix.cashlytics.core.providers.kredobank.extractors.BalanceStateExtractor
import com.x1unix.cashlytics.core.providers.kredobank.extractors.DateExtractor
import com.x1unix.cashlytics.core.providers.kredobank.extractors.PaymentDataExtractor

class KredoMessageParser : MessageParser {
    override val sender: String
        get() = "KREDOBANK"

    override val brandIcon: Int
        get() = R.drawable.kredobank

    private val dateExtractor = DateExtractor()
    private val paymentDataExtractor = PaymentDataExtractor()
    private val balanceStateExtractor = BalanceStateExtractor()

    override fun parseMessage(message: String): PaymentEvent {
        // Stage 1: Get date and time
        val parsedDate = dateExtractor.extractData(message, false)
        val date = parsedDate.result

        // Stage 2: Get payment metadata (type and recipient)
        val parsedMetadata = paymentDataExtractor.extractData(parsedDate.nextUnprocessedChunk, false)
        val paymentMetadata = parsedMetadata.result

        // Stage 3: Get balance change data
        val extractOnlyBaseData = paymentMetadata.type == PaymentType.Revert
        val parsedChanges = balanceStateExtractor.extractData(parsedMetadata.nextUnprocessedChunk, extractOnlyBaseData)
        val balanceState = parsedChanges.result

        return PaymentEvent(sender, date, paymentMetadata, balanceState)
    }

    override fun onWalletUpdate(newEvent: PaymentEvent): Boolean {
        return newEvent.type !== PaymentType.Revert
    }

    override fun buildWallet(event: PaymentEvent, context: BankingMessagesService): Wallet {
        // Default behavior for non-revert actions
        if (event.type !== PaymentType.Revert) {
            return Wallet.fromEvent(event)
        }

        // Otherwise, fill full wallet status using all events in timeline
        val allEvents = context.getProviderHistory(sender)
        val iterator = allEvents.iterator()

        val w = Wallet.fromEvent(event)
        w.status = event.changes.charged ?: Amount.empty()

        if (iterator.hasNext()) {
            buildWalletDataFromAllEvents(iterator, w)
        }

        return w
    }

    /**
     * Iterates over events to get wallet amount status.
     * Used when the last event doesn't have all amount status data (e.g. Revert event)
     */
    private fun buildWalletDataFromAllEvents(i: Iterator<PaymentEvent>, w: Wallet) {
        val e = i.next()

        if (e.type !== PaymentType.Revert) {

            if (e.changes.left?.currency != e.changes.left?.currency) {
                return
            }

            w.status.amount += e.changes.left!!.amount
        } else {
            w.status.amount += e.changes.charged!!.amount

            if (i.hasNext()) {
                buildWalletDataFromAllEvents(i, w)
            }
        }
    }


}