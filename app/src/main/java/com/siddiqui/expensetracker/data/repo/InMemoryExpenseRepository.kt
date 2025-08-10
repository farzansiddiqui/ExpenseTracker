package com.siddiqui.expensetracker.data.repo

import com.siddiqui.expensetracker.data.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object InMemoryExpenseRepository: ExpenseRepository {
    private val expenses = MutableStateFlow<List<Expense>>(emptyList())
    override suspend fun addExpense(expense: Expense) {
        expenses.update { it + expense }
    }

    override fun observeExpenses(): Flow<List<Expense>> = expenses.asStateFlow()

    override suspend fun getExpensesForDate(dateMillis: Long): List<Expense> {
        val startOfDay = dateMillis - (dateMillis % (24 * 60 * 60 * 1000))
        val endOfDay = startOfDay + (24 * 60 * 60 * 1000)
        return expenses.value.filter { it.dateMillis in startOfDay until endOfDay }
    }

}