package com.x1unix.cashlytics.kredobank

import com.x1unix.cashlytics.core.exceptions.DataParseException
import com.x1unix.cashlytics.core.exceptions.ParseException
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
            val result = extractor.extractData(message, false)

            // Check for correct date output
            assertEquals(expected, fmt.print(result.result))

            // Check for correct substring output
            assertEquals(expectedSubstring, result.nextUnprocessedChunk)
        } catch (ex: DataParseException) {
            fail("Failed to parse date '${ex.source}': ${ex.message}")
        }
    }

    @Test
    fun parseAltDataFromRevertMessage() {
        val date = "23.07.2018"
        val time = "19:43"
        val expected = "$date $time"
        val message = Messages.withDateAlt(date, time, Messages.Revert)

        val extractor = DateExtractor()
        val fmt = extractor.formatter

        try {
            val result = extractor.extractData(message, false)
            assertEquals(expected, fmt.print(result.result))
        } catch (ex: ParseException) {
            fail("failed to parse date '${ex.source}': ${ex.message}")
        }
    }
}