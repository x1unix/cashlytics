package com.x1unix.cashlytics.kredobank

import com.x1unix.cashlytics.events.PaymentMetadata
import com.x1unix.cashlytics.events.PaymentType
import com.x1unix.cashlytics.kredobank.fixtures.Messages
import com.x1unix.cashlytics.providers.kredobank.extractors.PaymentDataExtractor
import org.junit.Test

import org.junit.Assert.*
import kotlin.math.exp

const val BANK_NAME = "KREDOBANK"

class PaymentMetadataExtractorTest {
    private val extractor = PaymentDataExtractor()

    @Test
    fun parseInternetPurchase() {
        val expected = PaymentMetadata(PaymentType.Internet, "GOOGLE *Google Music")
        val result = extractor.extractData(Messages.InternetPurchase)

        assertEquals(result.result, expected)
    }

    @Test
    fun parseOfflinePurchase() {
        val expected = PaymentMetadata(PaymentType.Purchase, "MAGAZINPRODUKTIVARSEN")
        val result = extractor.extractData(Messages.Purchase)

        assertEquals(result.result, expected)
    }

    @Test
    fun parseTransfer() {
        val expected = PaymentMetadata(PaymentType.Transfer, BANK_NAME)
        val result = extractor.extractData(Messages.Transfer)

        assertEquals(result.result, expected)
    }

    @Test
    fun parseWithdrawal() {
        val expected = PaymentMetadata(PaymentType.Withdrawal, BANK_NAME)
        val result = extractor.extractData(Messages.Withdrawal)

        assertEquals(result.result, expected)
    }

    @Test
    fun parseRefill() {
        val expected = PaymentMetadata(PaymentType.Refill, BANK_NAME)
        val result = extractor.extractData(Messages.Refill)

        assertEquals(result.result, expected)
    }


}