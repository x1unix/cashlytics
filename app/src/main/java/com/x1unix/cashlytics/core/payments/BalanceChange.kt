package com.x1unix.cashlytics.core.payments

/**
 * Balance change record
 *
 * @param charged Charged amount
 * @param left Balance without overdraft
 * @param overdraft Overdraft
 * @param available Total available funds including overdraft
 */
data class BalanceChange(
        var charged: Amount? = null,
        var left: Amount? = null,
        var overdraft: Amount? = null,
        var available: Amount? = null
)