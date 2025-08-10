package com.siddiqui.expensetracker.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siddiqui.expensetracker.data.model.Expense
import com.siddiqui.expensetracker.data.repo.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExpenseListViewModel(private val repo: ExpenseRepository): ViewModel() {
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses

    init {
observeExpenses()
    }
    private fun observeExpenses() {
        viewModelScope.launch {
            repo.observeExpenses().collect { list ->
                _expenses.value = list
            }
        }
    }

    fun loadExpensesForDate(dateMillis: Long) {
        viewModelScope.launch {
            _expenses.value = repo.getExpensesForDate(dateMillis)
        }
    }
}