package com.x1unix.cashlytics.kredobank

import com.x1unix.cashlytics.exceptions.DataParseException
import com.x1unix.cashlytics.providers.kredobank.extractors.DateExtractor
import net.danlew.android.joda.JodaTimeAndroid
import org.junit.Test

import org.junit.Assert.*

class DateExtractorTest {
    @Test
    fun parseDataFromTransactionMessage() {
        val message = "27.06.2018 19:12 KREDOBANK PEREKAZ 1000.00UAH Z  KARTY **2111 ZALISHOK 26174.35 UAH OVERDRAFT 50000.00 UAH DOSTUPNO 76174.35UAH"
        val expected = "27.06.2018 19:12"

        val extractor = DateExtractor()
        val fmt = extractor.formatter
        val expectedSubstring = message.substring(expected.length).trim()

        try {
            val result = extractor.extractData(message)

            // Check for correct date output
            assertEquals(expected, fmt.print(result.result))

            // Check for correct substring output
            assertEquals(expectedSubstring, result.nextUnprocessedChunk)
        } catch (ex: DataParseException) {
            fail("Failed to parse date '${ex.source}': ${ex.message}");
        }
    }
}