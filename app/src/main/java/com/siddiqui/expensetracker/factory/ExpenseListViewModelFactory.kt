package com.siddiqui.expensetracker.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.siddiqui.expensetracker.data.repo.ExpenseRepository
import com.siddiqui.expensetracker.vm.ExpenseListViewModel

class ExpenseListViewModelFactory(
    private val repo: ExpenseRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseListViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}