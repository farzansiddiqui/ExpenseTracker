package com.siddiqui.expensetracker.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.siddiqui.expensetracker.data.repo.ExpenseRepository
import com.siddiqui.expensetracker.data.repo.InMemoryExpenseRepository
import com.siddiqui.expensetracker.vm.ExpenseEntryViewModel

class ExpenseEntryViewModelFactory(
    private val repo: ExpenseRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseEntryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseEntryViewModel(repo as InMemoryExpenseRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}