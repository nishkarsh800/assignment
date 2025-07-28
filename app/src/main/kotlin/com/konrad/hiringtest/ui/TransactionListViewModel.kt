package com.konrad.hiringtest.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.konrad.hiringtest.model.Result
import com.konrad.hiringtest.model.ui.Transaction
import com.konrad.hiringtest.repository.TransactionRepository
import com.konrad.hiringtest.util.TransactionUtilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionListViewModel(
    private val bkoTransactionRepository: TransactionRepository,
    private val kibkTransactionRepository: TransactionRepository,
    private val rbkTransactionRepository: TransactionRepository,
) : ViewModel() {

    /**
     * Represents a list of transactions. Each child list is a list of transactions grouped by their data source (i.e. BKO, KIBK, RBK, KD)
     */
    private val networkTransactions = MutableSharedFlow<List<List<Transaction>>>()

    /**
     * Represents the search value. We filter the list of transactions via this value.
     */
    private val searchValue = MutableStateFlow("")

    /**
     * Represents the input text value to display in our SearchAppBar.
     */
    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> get() = _inputText.asStateFlow()

    /**
     * Combines the search value and transactions and sorts to return a dataSource to populate the list.
     */
    private val _dataSource: StateFlow<List<Transaction>> =
        combine(networkTransactions, searchValue) { networkTransactions, searchText ->
            val currentTransactionList =
                TransactionUtilities.combineAndSortTransactions(networkTransactions)
            if (searchText.length >= 3) {
                currentTransactionList.filter {
                    it.description.contains(searchText, ignoreCase = true)
                }
            } else {
                currentTransactionList
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    val dataSource: StateFlow<List<Transaction>> = _dataSource

    init {
        // Debouncing the input and updating searchValue
        viewModelScope.launch {
            _inputText
                .debounce(250)
                .distinctUntilChanged()
                .collect { searchValue.emit(it) }
        }
        getAllTransactions()
    }

    /**
     * Notify the view model that the search text has changed and that we need to modify the data source entries.
     *
     * @param searchText The string coming in from the search text view.
     */
    fun setInputText(searchText: String) {
        viewModelScope.launch {
            _inputText.emit(searchText)
        }
    }

    /**
     * Fetch all transactions from all sources.
     */
    fun getAllTransactions() {
        viewModelScope.launch(Dispatchers.IO) {
            val bkoTransactions = async { getTransactionsFromRepository(bkoTransactionRepository) }
            val kibkTransactions = async { getTransactionsFromRepository(kibkTransactionRepository) }
            val rbkTransactions = async { getTransactionsFromRepository(rbkTransactionRepository) }

            networkTransactions.emit(
                listOf(
                    bkoTransactions.await(),
                    kibkTransactions.await(),
                    rbkTransactions.await(),
                )
            )
        }
    }

    private suspend fun getTransactionsFromRepository(transactionRepository: TransactionRepository): List<Transaction> {
        return when (val result = transactionRepository.getAllTransactions()) {
            is Result.Success -> {
                result.data
            }
            is Result.Error -> {
                emptyList()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val bkoTransactionRepository: TransactionRepository,
        private val kibkTransactionRepository: TransactionRepository,
        private val rbkTransactionRepository: TransactionRepository,
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = TransactionListViewModel(
            bkoTransactionRepository,
            kibkTransactionRepository,
            rbkTransactionRepository,
        ) as T
    }
}
