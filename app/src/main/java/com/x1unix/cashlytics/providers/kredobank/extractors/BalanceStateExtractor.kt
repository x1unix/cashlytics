package com.x1unix.cashlytics.providers.kredobank.extractors

/**
 * Balance state regular expression.
 * Returns 3 matches with 4 groups
 *
 * 1 group is full amount string (e.g. 12345.00UAH)
 * 2 group is amount number without currency
 * 3 group is the currency. Present only at overdraft and charge amount.
 * 4 group also currency for left balance and overdraft.
 *
 * ([\d]+\.[\d]+([A-Z]{1,3})?)
 */
const val BALANCE_STATE_REGEXP = "(([\\d]+\\.[\\d]+)+([A-Z]{1,3})?(\\s[A-Z]{1,3}+)?)";

class BalanceStateExtractor {
}