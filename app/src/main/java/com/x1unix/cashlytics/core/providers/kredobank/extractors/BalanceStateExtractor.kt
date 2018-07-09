package com.x1unix.cashlytics.core.providers.kredobank.extractors

import com.x1unix.cashlytics.core.exceptions.DataParseException
import com.x1unix.cashlytics.core.exceptions.NoMatchFoundException
import com.x1unix.cashlytics.core.payments.Amount
import com.x1unix.cashlytics.core.payments.BalanceChange
import com.x1unix.cashlytics.core.providers.MetadataExtractor
import com.x1unix.cashlytics.core.providers.MetadataParseResult
import java.util.regex.Matcher
import java.util.regex.Pattern

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
private const val BALANCE_STATE_REGEXP = "(([\\d]+\\.[\\d]+)+([A-Z]{1,3})?(\\s[A-Z]{1,3}+)?)"

/**
 * Charged amount regex group
 */
private const val CHARGED = 0

/**
 * Funds left regex group
 */
private const val LEFT = 1

/**
 * Overdraft regex group
 */
private const val OVERDRAFT = 2

/**
 * Total available funds regex group
 */
private const val AVAILABLE = 3

/**
 * Initial default currency
 */
private const val INITIAL_DEFAULT_CURRENCY = "UAH"

/**
 * Balance state extractor
 */
class BalanceStateExtractor: MetadataExtractor<BalanceChange> {
    private val balanceStatePattern = Pattern.compile(BALANCE_STATE_REGEXP)
    private var defaultCurrency = INITIAL_DEFAULT_CURRENCY

    override fun extractData(message: String): MetadataParseResult<BalanceChange> {
        val matcher = balanceStatePattern.matcher(message)
        val balanceChange = BalanceChange()

        var currentAmountIndex = CHARGED

        while (matcher.find()) {
            val amount = extractBalanceFromMatch(matcher, currentAmountIndex)

            when (currentAmountIndex) {
                CHARGED -> balanceChange.charged = amount
                LEFT -> balanceChange.left = amount
                OVERDRAFT -> balanceChange.overdraft = amount
                AVAILABLE -> balanceChange.available = amount
            }
            currentAmountIndex++
        }

        // Reset default currency
        defaultCurrency = INITIAL_DEFAULT_CURRENCY

        if (currentAmountIndex == 0) {
            throw NoMatchFoundException("Cannot find any balance information in string")
        }

        return MetadataParseResult(balanceChange, message, message)
    }

    private fun extractBalanceFromMatch(matcher: Matcher, matchIndex: Number): Amount {
        val groupCount = matcher.groupCount()
        if ((matchIndex == CHARGED) || (matchIndex == AVAILABLE)) {
            if (groupCount < 3) {
                throw DataParseException("Group count mismatch for amount type $matchIndex", matcher.group())
            }

            val amount = matcher.group(2)
            val currency = matcher.group(3)

            return Amount(amount.toDouble(), currency)
        }

        if ((matchIndex == LEFT) || (matchIndex == OVERDRAFT)) {
            if (groupCount < 4) {
                throw DataParseException("Group count mismatch for amount type $matchIndex", matcher.group())
            }

            val amount = matcher.group(2)

            // Currency is not populated for overdraft messages, so use default currency as fallback
            val currency = matcher.group(4) ?: defaultCurrency

            if (matchIndex == LEFT) {
                // Set default currency from funds left result
                defaultCurrency = currency
            }

            return Amount(amount.toDouble(), currency.trim())
        }

        throw NoMatchFoundException("No match found for regex match for amount type $matchIndex")
    }
}