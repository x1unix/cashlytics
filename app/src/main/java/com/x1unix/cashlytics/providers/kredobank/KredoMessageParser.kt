package com.x1unix.cashlytics.providers.kredobank

import java.util.regex.Pattern;
import com.x1unix.cashlytics.events.PaymentEvent
import com.x1unix.cashlytics.exceptions.NoMatchException
import java.time.LocalDateTime

class KredoMessageParser {
    val sender: String
        get() = "KREDOBANK";

    fun parseMessage(message: String) : PaymentEvent {

        throw NotImplementedError();
    }


}