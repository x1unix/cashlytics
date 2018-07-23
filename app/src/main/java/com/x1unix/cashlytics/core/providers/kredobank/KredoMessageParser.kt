package com.x1unix.cashlytics.core.providers.kredobank

import com.x1unix.cashlytics.R
import com.x1unix.cashlytics.core.payments.PaymentEvent
import com.x1unix.cashlytics.core.payments.PaymentType
import com.x1unix.cashlytics.core.providers.MessageParser
import com.x1unix.cashlytics.core.providers.kredobank.extractors.BalanceStateExtractor
import com.x1unix.cashlytics.core.providers.kredobank.extractors.DateExtractor
import com.x1unix.cashlytics.core.providers.kredobank.extractors.PaymentDataExtractor

class KredoMessageParser: MessageParser {
    override val sender: String
        get() = "KREDOBANK"

    override val brandIcon: Int
        get() = R.drawable.kredobank

    private val dateExtractor = DateExtractor()
    private val paymentDataExtractor = PaymentDataExtractor()
    private val balanceStateExtractor = BalanceStateExtractor()

    override fun parseMessage(message: String) : PaymentEvent {
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


}