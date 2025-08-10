package com.siddiqui.expensetracker.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.siddiqui.expensetracker.data.repo.ExpenseRepository
import com.siddiqui.expensetracker.vm.ExpenseReportViewModel

class ExpenseReportViewModelFactory(
    private val repo: ExpenseRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseReportViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseReportViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}