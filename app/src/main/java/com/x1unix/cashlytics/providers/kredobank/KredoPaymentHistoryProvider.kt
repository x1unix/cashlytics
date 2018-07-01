package com.x1unix.cashlytics.providers.kredobank

import com.x1unix.cashlytics.providers.PaymentHistoryProvider;
import com.x1unix.cashlytics.providers.kredobank.extractors.DateExtractor
import org.joda.time.LocalDateTime

class KredoPaymentHistoryProvider : PaymentHistoryProvider {

    val dateExtractor = DateExtractor()
    override val name: String
        get() = "KredoBank";

    constructor() {
    }


}