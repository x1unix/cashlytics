package com.x1unix.cashlytics.providers.kredobank

import com.x1unix.cashlytics.events.PaymentEvent
import com.x1unix.cashlytics.providers.kredobank.extractors.DateExtractor

class KredoMessageParser {
    val sender: String
        get() = "KREDOBANK";

    val dateExtractor = DateExtractor()

    fun parseMessage(message: String) : PaymentEvent {
        val parsedDate = dateExtractor.extractData(message)
        throw NotImplementedError();
    }


}