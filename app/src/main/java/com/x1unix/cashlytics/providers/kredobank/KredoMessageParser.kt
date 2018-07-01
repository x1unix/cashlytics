package com.x1unix.cashlytics.providers.kredobank

import com.x1unix.cashlytics.events.PaymentEvent
import com.x1unix.cashlytics.providers.kredobank.extractors.DateExtractor
import com.x1unix.cashlytics.providers.kredobank.extractors.PaymentDataExtractor

class KredoMessageParser {
    val sender: String
        get() = "KREDOBANK";

    private val dateExtractor = DateExtractor()
    private val paymentDataExtractor = PaymentDataExtractor()

    fun parseMessage(message: String) : PaymentEvent {
        // Stage 1: Get date and time
        val parsedDate = dateExtractor.extractData(message)
        val date = parsedDate.result;

        // Stage 2: Get payment metadata (type and recipient)
        val parsedMetadata = paymentDataExtractor.extractData(parsedDate.nextUnprocessedChunk)
        val paymentMetadata = parsedMetadata.result

        return PaymentEvent(date, paymentMetadata)
    }


}