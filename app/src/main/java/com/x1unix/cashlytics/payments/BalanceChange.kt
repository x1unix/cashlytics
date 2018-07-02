package com.x1unix.cashlytics.payments

/**
 * Balance change record
 *
 * @param charged Charged amount
 * @param left Balance without overdraft
 * @param overdraft Overdraft
 * @param available Total available funds including overdraft
 */
data class BalanceChange(val charged: Amount, val left: Amount, val overdraft: Amount, val available: Amount)