package com.x1unix.cashlytics.kredobank.fixtures

import com.x1unix.cashlytics.payments.Amount
import com.x1unix.cashlytics.payments.BalanceChange

object Balance {
    private const val UAH = "UAH"

    const val Transfer = "1000.00UAH Z  KARTY **2111 ZALISHOK 26174.35 UAH OVERDRAFT 50000.00 UAH DOSTUPNO 76174.35UAH"
    const val Withdrawal = "400.00UAH CARD**2111 ZALISHOK 25774.35 UAH OVERDRAFT50000.00 UAH DOSTUPNO 75774.35UAH"
    const val Purchase = ", UA 1267.18UAH CARD**2111 ZALISHOK 24507.17 UAH OVER 50000.00  DOSTUPNO 74507.17UAH"
    const val InternetPurchase = ", GB 59.00UAH CARD**2111 ZALISHOK 22805.84 UAH OVER 50000.00  DOSTUPNO 72805.84UAH"
    const val Refill = "65789.14UAH CARD**2111 ZALISHOK 27723.75 UAH OVER 50000.00 UAH DOSTUPNO 77723.75UAH"
    const val Debit = "193.08UAH CARD**2111 ZALISHOK 27530.67 UAH OVERDRAFT 50000.00 UAH DOSTUPNO 77530.67UAH"

    val TransferChange: BalanceChange
        get() = BalanceChange(uah(1000.0), uah(26174.35), uah(50000.0), uah(76174.35))

    private fun uah(amount: Double): Amount {
        return Amount(amount, UAH)
    }
}