package com.konrad.hiringtest

import com.konrad.hiringtest.model.ui.Transaction
import com.konrad.hiringtest.util.TransactionUtilities
import org.junit.Assert.assertEquals
import org.junit.Test

class TransactionUtilitiesTest {

    @Test
    fun `combineAndSortTransactions with empty list input returns an empty list`() {
        val combinedTransactions = TransactionUtilities.combineAndSortTransactions(emptyList())

        assertEquals(emptyList<List<Transaction>>(), combinedTransactions)
    }
}