package com.konrad.hiringtest.ui

import android.icu.text.DecimalFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.konrad.hiringtest.model.ui.BankType
import com.konrad.hiringtest.model.ui.Transaction
import java.math.BigDecimal
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
private val decimalFormatter = DecimalFormat("$###0.00")

/**
 * A composable to display a list of transactions.
 *
 * This list should include a search bar at the top by which you can filter results,
 * followed by the list of transactions that follow closely to the example provided
 * in the questions.
 *
 * The list of transactions should be retrieved by TransactionListViewModel.
 *
 * The list should also have the ability to be 'refreshed' which will perform the retrieval call again before displaying.
 *
 * @param transactionListViewModel The view model to access the list of transactions which it is retrieving from a 3rd party API.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionList(
    transactionListViewModel: TransactionListViewModel
) {
    val transactions by transactionListViewModel.dataSource.collectAsState()
    val pullRefreshState = rememberPullToRefreshState()
    Column {
        SearchAppBar(
            placeholderText = "Search...",
            inactiveText = "KG Bank Aggregator",
        )
        PullToRefreshBox(
            state = pullRefreshState,
            isRefreshing = false,
            onRefresh = { }
        ) {
            LazyColumn(
                contentPadding = WindowInsets.navigationBars.asPaddingValues(),
            ) {
                items(transactions) { transaction ->
                    TransactionListItem(transaction)
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun TransactionListItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            // Date
            Text(
                text = transaction.date.format(dateFormatter)
            )
            // Description
            Text(
                text = transaction.description
            )
            // Value
            Text(
                text = decimalFormatter.format(transaction.value),
            )
            // Bank Type
            Text(
                text = transaction.source.toString()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionListItemPreview() {
    TransactionListItem(
        transaction = Transaction(
            date = java.time.LocalDate.now(),
            value = BigDecimal(100),
            description = "Test Transaction",
            tags = listOf("Tag 1", "Tag 2"),
            source = BankType.BKO
        )
    )
}