package com.x1unix.cashlytics.kredobank

import com.x1unix.cashlytics.kredobank.fixtures.Balance
import com.x1unix.cashlytics.providers.kredobank.extractors.BalanceStateExtractor
import org.junit.Test
import org.junit.Assert.*

class BalanceStateExtractorTest {
    private val extractor = BalanceStateExtractor();

    @Test
    fun parseBalanceFromTransferMessage() {
        val message = Balance.Transfer
        val expected = Balance.TransferChange

        val got = extractor.extractData(message)

        assertEquals(expected, got)
    }
}