package com.siddiqui.expensetracker.data.repo

import com.siddiqui.expensetracker.data.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun addExpense(expense: Expense)
    fun observeExpenses(): Flow<List<Expense>>
    suspend fun getExpensesForDate(dateMillis: Long): List<Expense>
}