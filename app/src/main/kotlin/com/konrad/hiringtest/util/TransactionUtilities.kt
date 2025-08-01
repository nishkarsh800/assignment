package com.konrad.hiringtest.util

import com.konrad.hiringtest.model.ui.Transaction

object TransactionUtilities {

    /**
     * Given a list of transaction lists provided by multiple bank sources, manually flatten/combine the list, and sort by
     * date in reverse chronologically order (i.e. earliest to oldest).
     *
     * This operation is done recursively.
     *
     * @param transactions List of transaction lists provided by multiple bank sources.
     *
     * @return The list of flatten and sorted transactions.
     */
    fun combineAndSortTransactions(transactions: List<List<Transaction>>): List<Transaction> {
        return mergeSortedLists(emptyList(), transactions)
    }

    /**
     * Helper method to recursively flatten and sort the transaction list. This method pulls the first list out to sort.
     *
     * @param firstList The first list of transactions which are already sorted in reverse chronological order.
     * @param restOfLists The remaining lists of transactions which need to be sorted.
     *
     * @return The flatten and sorted result of [firstList] and [restOfLists].
     */
    private fun mergeSortedLists(
        firstList: List<Transaction>,
        restOfLists: List<List<Transaction>>
    ): List<Transaction> {
        val nextList = restOfLists.firstOrNull() ?: return firstList
        val nextListSorted = nextList.sortedByDescending { it.date }
        val secondList = mergeSortedLists(nextListSorted, restOfLists.drop(1))

        return combineTwoSortedLists(firstList, secondList)
    }

    /**
     * Given two transaction lists, flatten and sort them into one transaction list in reverse chronological order
     * (i.e. earliest to oldest).
     * 
     * This method should operate in O(n) time complexity.
     *
     * @param list1 The first list of transactions in reverse chronological order.
     * @param list2 The second list of transactions in reverse chronological order.
     *
     * @return The combination of [list1] and [list2] as a sorted list.
     */
    private fun combineTwoSortedLists(
        list1: List<Transaction>,
        list2: List<Transaction>
    ): List<Transaction> {
        val result = mutableListOf<Transaction>()
        var i = 0
        var j = 0

        //BASIC PRINCIPLE: TWO WAY MERGE ALGORITHM
        // Merge the two sorted lists using two pointers
        while (i < list1.size && j < list2.size) {
            // Compare dates in reverse chronological order (latest first)
            if (list1[i].date >= list2[j].date) {
                result.add(list1[i])
                i++
            } else {
                result.add(list2[j])
                j++
            }
        }
        
        // Add remaining elements from list1
        while (i < list1.size) {
            result.add(list1[i])
            i++
        }
        
        // Add remaining elements from list2
        while (j < list2.size) {
            result.add(list2[j])
            j++
        }
        
        return result
    }
}