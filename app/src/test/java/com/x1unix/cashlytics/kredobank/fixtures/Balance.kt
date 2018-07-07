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
    const val Debit = "193.08UAH CARD**2111 ZALISHOK 27530.67 UAH OVERDRAFT 45000.00 UAH DOSTUPNO 77530.67UAH"

    val TransferChange: BalanceChange
        get() = BalanceChange(uah(1000.0), uah(26174.35), uah(50000.0), uah(76174.35))

    val WithdrawalChange: BalanceChange
        get() = BalanceChange(uah(400.0), uah(25774.35), uah(50000.00), uah(75774.35))

    val PurchaseChange: BalanceChange
        get() = BalanceChange(uah(1267.18), uah(24507.17), uah(50000.00), uah(74507.17))

    val InternetPurchaseChange: BalanceChange
        get() = BalanceChange(uah(59.0), uah(22805.84), uah(50000.00), uah(72805.84))

    val RefillChange: BalanceChange
        get() = BalanceChange(uah(65789.14), uah(27723.75), uah(50000.00), uah(77723.75))

    val DebitChange: BalanceChange
        get() = BalanceChange(uah(193.08), uah(27530.67), uah(45000.00), uah(77530.67))



    private fun uah(amount: Double): Amount {
        return Amount(amount, UAH)
    }
}