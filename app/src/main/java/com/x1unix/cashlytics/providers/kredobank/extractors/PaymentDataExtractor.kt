package com.x1unix.cashlytics.providers.kredobank.extractors

import com.x1unix.cashlytics.events.PaymentMetadata
import com.x1unix.cashlytics.events.PaymentType
import com.x1unix.cashlytics.exceptions.NoMatchFoundException
import com.x1unix.cashlytics.providers.MetadataExtractor
import com.x1unix.cashlytics.providers.MetadataParseResult
import java.util.regex.Pattern

/**
 * Pattern for internal transactions (withdrawal, refill, card-to-card).
 * These messages prefixed with KREDOBANK word.
 */
const val TRANSACTION_PATTERN = """(KREDOBANK\s([A-Z\s]+))"""

/**
 * Expected group size of matches from KREDO_TRANSACTION_PATTERN
 */
const val TRANSACTION_GROUP_SZ = 2;

/**
 * Pattern for rest of payment actions.
 * Should go just right after the action date
 */
const val PAYMENT_PATTERN = """(((\w+){1}(.+)),\s)"""

/**
 * Expected group size of matches from PAYMENT_PATTERN
 */
const val PAYMENT_GROUP_SZ = 2;

const val BANK_NAME = "KREDOBANK"

// Payment types
const val TRANSFER = "PEREKAZ"
const val WITHDRAWAL = "ZNYATIA HOTIVKY"
const val INTERNET = "INTERNET"
const val PURCHASE = "KUPIVLIA"
const val DEBIT = "SPYSANIA"
const val REFILL = "ZARAKHUVANIA"

/**
 * Extracts payment type and receiver information
 *
 * @implements MetadataExtractor
 */
class PaymentDataExtractor : MetadataExtractor<PaymentMetadata> {

    private val transactionPattern = Pattern.compile(TRANSACTION_PATTERN)

    private val paymentPattern = Pattern.compile(PAYMENT_PATTERN)

    override fun extractData(message: String) : MetadataParseResult<PaymentMetadata> {
        val transactionMatcher = transactionPattern.matcher(message)

        // Detect bank transaction action
        if (transactionMatcher.find()) {
            // Check if all data from regex is available
            val groupSize = transactionMatcher.groupCount()
            if (groupSize < TRANSACTION_GROUP_SZ) {
                throw NoMatchFoundException("transaction pattern found, but expected group size is not correct ($groupSize)")
            }

            val paymentType = getPaymentType(transactionMatcher.group(2)) // Second group contains payment type

            val metadata = PaymentMetadata(paymentType, BANK_NAME)

            return MetadataParseResult(metadata, transactionMatcher.group(0), message)
        }

        // Detect rest payment operations
        val paymentMatcher = paymentPattern.matcher(message)
        if (paymentMatcher.find()) {
            // Check if all data from regex is available
            val groupSize = paymentMatcher.groupCount()
            if (groupSize < PAYMENT_GROUP_SZ) {
                throw NoMatchFoundException("payment pattern found, but expected group size is not correct ($groupSize)")
            }

            val paymentPrefix = paymentMatcher.group(3)
            val paymentType = getPaymentType(paymentPrefix) // Second group is payment type
            val paymentReceiver = paymentMatcher.group(4).trim()   // Forth group contains the payment receiver name

            val metadata = PaymentMetadata(paymentType, paymentReceiver)

            return MetadataParseResult(metadata, paymentMatcher.group(0), message);
        }

        // Last case - try to get operation name from first word (used for refill messages)
        // Currently is workaround.
        val chunks = message.split(' ');
        if (!chunks.isEmpty()) {
            val type = getPaymentType(chunks[0])

            if (type != PaymentType.Unknown) {
                val metadata = PaymentMetadata(type, BANK_NAME)
                return MetadataParseResult(metadata, chunks[0] + " ", message)
            }
        }

        // Otherwise - throw an error
        throw NoMatchFoundException("no any pattern match found for this message");
    }

    private fun getPaymentType(origin: String) : PaymentType {
        val normalized = origin.trim().toUpperCase()

        return when (normalized) {
            TRANSFER -> PaymentType.Transfer
            WITHDRAWAL -> PaymentType.Withdrawal
            INTERNET -> PaymentType.Internet
            PURCHASE -> PaymentType.Purchase
            DEBIT -> PaymentType.Debit
            REFILL -> PaymentType.Refill
            else -> PaymentType.Unknown
        }
    }
}