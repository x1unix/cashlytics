package com.x1unix.cashlytics.kredobank

import com.x1unix.cashlytics.events.PaymentMetadata
import com.x1unix.cashlytics.events.PaymentType
import com.x1unix.cashlytics.kredobank.fixtures.Messages
import com.x1unix.cashlytics.providers.kredobank.extractors.PaymentDataExtractor
import org.junit.Test

import org.junit.Assert.*

class PaymentMetadataExtractorTest {
    @Test
    fun parseInternetPurchase() {
        val expected = PaymentMetadata(PaymentType.Internet, "GOOGLE *Google Music")
        val extractor = PaymentDataExtractor()

        val result = extractor.extractData(Messages.InternetPurchase)

        assertEquals(result.result, expected)

    }
}