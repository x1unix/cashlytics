package com.x1unix.cashlytics.providers.kredobank

import com.x1unix.cashlytics.providers.PaymentHistoryProvider;

class KredoPaymentHistoryProvider : PaymentHistoryProvider {

    override val name: String
        get() = "KredoBank";

    constructor() {
    }


}