package com.konrad.hiringtest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konrad.hiringtest.model.Result
import com.konrad.hiringtest.model.ui.BankType
import com.konrad.hiringtest.model.ui.Transaction
import com.konrad.hiringtest.ui.theme.transactionListEntryNegativeText
import com.konrad.hiringtest.ui.theme.transactionListEntryPositiveText
import com.konrad.hiringtest.ui.theme.transactionListEntrySourceBko
import com.konrad.hiringtest.ui.theme.transactionListEntrySourceKd
import com.konrad.hiringtest.ui.theme.transactionListEntrySourceKibk
import com.konrad.hiringtest.ui.theme.transactionListEntrySourceRbk
import com.konrad.hiringtest.ui.theme.transactionListEntryTagText
import java.math.BigDecimal
import java.text.DecimalFormat
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
    val dataState by transactionListViewModel.dataState.collectAsState()
    val inputText by transactionListViewModel.inputText.collectAsState()
    val pullRefreshState = rememberPullToRefreshState()
    Column {
        SearchAppBar(
            placeholderText = "Search...",
            inactiveText = "KG Bank Aggregator",
            value = inputText,
            onValueChange = { transactionListViewModel.setInputText(it) }
        )
        when (dataState) {
            is Result.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is Result.Error -> {
                PullToRefreshBox(
                    state = pullRefreshState,
                    isRefreshing = false,
                    onRefresh = { transactionListViewModel.getAllTransactions() }
                ) {
                    if ((dataState as Result.Error).throwable?.message == "NO_INTERNET") {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "OOPS :(",
                                    style = androidx.compose.material3.MaterialTheme.typography.displayMedium,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                Text(
                                    text = "No Internet Present",
                                    style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text(
                                    text = "Please turn on your internet connection to view transactions.",
                                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(bottom = 24.dp)
                                )
                                Button(
                                    onClick = { transactionListViewModel.getAllTransactions() }
                                ) {
                                    Text("Retry")
                                }
                            }
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Failed to load transactions.")
                        }
                    }
                }
            }
            is Result.Success -> {
                PullToRefreshBox(
                    state = pullRefreshState,
                    isRefreshing = false,
                    onRefresh = { }
                ) {
                    Column {
                        // Debug info
                        Text(
                            text = "Loaded ${transactions.size} transactions",
                            modifier = Modifier.padding(16.dp),
                            style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        
                        // Show sample transactions by bank
                        val kdTransactions = transactions.filter { it.source == BankType.KD }
                        val bkoTransactions = transactions.filter { it.source == BankType.BKO }
                        val kibkTransactions = transactions.filter { it.source == BankType.KIBK }
                        val rbkTransactions = transactions.filter { it.source == BankType.RBK }
                        
                        Text(
                            text = "KD: ${kdTransactions.size}, BKO: ${bkoTransactions.size}, KIBK: ${kibkTransactions.size}, RBK: ${rbkTransactions.size}",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                            style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        
                        if (kdTransactions.isNotEmpty()) {
                            Text(
                                text = "Sample KD: ${kdTransactions.first().description}",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
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
        }
    }
}

@Composable
fun TransactionListItem(transaction: Transaction) {
    val valueColor = if (transaction.value < BigDecimal.ZERO) {
        transactionListEntryNegativeText
    } else {
        transactionListEntryPositiveText
    }
    val bankColor = when (transaction.source) {
        BankType.KIBK -> transactionListEntrySourceKibk
        BankType.BKO -> transactionListEntrySourceBko
        BankType.RBK -> transactionListEntrySourceRbk
        BankType.KD -> transactionListEntrySourceKd
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Bank type badge
                Surface(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(bankColor),
                    color = bankColor,
                    shape = CircleShape,
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = transaction.source.name,
                            color = Color.White,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                // Date
                Text(
                    text = transaction.date.format(dateFormatter),
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Description
            Text(
                text = transaction.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Value
            Text(
                text = decimalFormatter.format(transaction.value),
                color = valueColor,
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Tags
            if (transaction.tags.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    transaction.tags.forEach { tag ->
                        Surface(
                            color = Color(0xFFF0F0F0),
                            shape = RoundedCornerShape(50),
                        ) {
                            Text(
                                text = tag,
                                color = transactionListEntryTagText,
                                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
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