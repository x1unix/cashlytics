package com.x1unix.cashlytics.kredobank

import com.x1unix.cashlytics.core.exceptions.DataParseException
import com.x1unix.cashlytics.kredobank.fixtures.Messages
import com.x1unix.cashlytics.core.providers.kredobank.extractors.DateExtractor
import org.junit.Test

import org.junit.Assert.*

class DateExtractorTest {
    @Test
    fun parseDataFromTransactionMessage() {
        val expected = "27.06.2018 19:43"
        val message = Messages.withDate(expected, Messages.Withdrawal)

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