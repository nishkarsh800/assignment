package com.konrad.hiringtest.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.konrad.hiringtest.di.ServiceProvider
import com.konrad.hiringtest.repository.DefaultBKOTransactionRepository
import com.konrad.hiringtest.repository.DefaultKIBKTransactionRepository
import com.konrad.hiringtest.repository.DefaultRBKTransactionRepository
import com.konrad.hiringtest.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    private val viewModel: TransactionListViewModel by viewModels {
        TransactionListViewModel.Factory(
            DefaultBKOTransactionRepository(ServiceProvider.bkoService),
            DefaultKIBKTransactionRepository(ServiceProvider.kibkService),
            DefaultRBKTransactionRepository(ServiceProvider.rbkService),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                TransactionList(viewModel)
            }
        }
    }
}