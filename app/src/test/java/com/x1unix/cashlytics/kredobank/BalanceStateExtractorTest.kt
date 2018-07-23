package com.x1unix.cashlytics.kredobank

import com.x1unix.cashlytics.kredobank.fixtures.Balance
import com.x1unix.cashlytics.core.providers.kredobank.extractors.BalanceStateExtractor
import org.junit.Test
import org.junit.Assert.*

class BalanceStateExtractorTest {
    private val extractor = BalanceStateExtractor()

    @Test
    fun parseBalanceFromTransferMessage() {
        val message = Balance.Transfer
        val expected = Balance.TransferChange

        val parseResult = extractor.extractData(message, false)
        val got = parseResult.result

        assertEquals(expected, got)
    }

    @Test
    fun parseBalanceFromWithdrawalMessage() {
        val message = Balance.Withdrawal
        val expected = Balance.WithdrawalChange

        val parseResult = extractor.extractData(message, false)
        val got = parseResult.result

        assertEquals(expected, got)
    }

    @Test
    fun parseBalanceFromPurchaseMessage() {
        val message = Balance.Purchase
        val expected = Balance.PurchaseChange

        val parseResult = extractor.extractData(message, false)
        val got = parseResult.result

        assertEquals(expected, got)
    }

    @Test
    fun parseBalanceFromInternetPurchaseMessage() {
        val message = Balance.InternetPurchase
        val expected = Balance.InternetPurchaseChange

        val parseResult = extractor.extractData(message, false)
        val got = parseResult.result

        assertEquals(expected, got)
    }

    @Test
    fun parseBalanceFromRefillMessage() {
        val message = Balance.Refill
        val expected = Balance.RefillChange

        val parseResult = extractor.extractData(message, false)
        val got = parseResult.result

        assertEquals(expected, got)
    }

    @Test
    fun parseBalanceFromDebitMessage() {
        val message = Balance.Debit
        val expected = Balance.DebitChange

        val parseResult = extractor.extractData(message, false)
        val got = parseResult.result

        assertEquals(expected, got)
    }

    @Test
    fun parseBalanceFromRevertMessage() {
        val message = Balance.Revert
        val expected = Balance.RevertChange
        val parseResult = extractor.extractData(message, true)
        val got = parseResult.result

        assertEquals(expected, got)
    }
}