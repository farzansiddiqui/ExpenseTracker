package com.siddiqui.expensetracker.vm

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siddiqui.expensetracker.data.model.Category
import com.siddiqui.expensetracker.data.model.Expense
import com.siddiqui.expensetracker.data.repo.InMemoryExpenseRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


data class EntryUiState(
    val title: String = "",
    val amount: String = "",
    val category: Category = Category.Staff,
    val notes: String = "",
    val receiptImageUri: String? = null,
    val totalToday: Double = 0.0,
)

class ExpenseEntryViewModel( private val repo: InMemoryExpenseRepository = InMemoryExpenseRepository): ViewModel() {
    private val _uiState = MutableStateFlow(EntryUiState())
    val uiState: StateFlow<EntryUiState> = _uiState

    fun onTitleChange(value: String) {
        _uiState.value = _uiState.value.copy(title = value)
    }

    fun onAmountChange(value: String) {
        _uiState.value = _uiState.value.copy(amount = value)
    }

    fun onCategoryChange(value: Category) {
        _uiState.value = _uiState.value.copy(category = value)
    }

    fun onNoteChange(value: String) {
        _uiState.value = _uiState.value.copy(notes = value)
    }

    suspend fun submitExpense(customDateMills: Long? = null): Boolean {
        val title = _uiState.value.title.trim()
        val amount = _uiState.value.amount.toDoubleOrNull()
        val category = _uiState.value.category
        val notes = _uiState.value.notes.takeIf { it.isNotBlank() }
        val receiptImageUri = _uiState.value.receiptImageUri
        val dateMillis = customDateMills ?: System.currentTimeMillis()

        if (title.isEmpty()) return false
        if (amount == null || amount <= 0) return false

        val existing = repo.getExpensesForDate(dateMillis).any {
            it.title.equals(title, ignoreCase = true) && it.amount == amount
        }
        if (existing) return false

        val expense = Expense(
            title = title,
            amount = amount,
            category = category,
            notes = notes,
            receiptUri = receiptImageUri,
            dateMillis = dateMillis
        )

        repo.addExpense(expense) // suspend DB insert
        updateTotalToday()
        return true
    }


    private fun updateTotalToday() {
        viewModelScope.launch {
            val today = System.currentTimeMillis()
            val total = repo.getExpensesForDate(today).sumOf { it.amount }
            _uiState.value = _uiState.value.copy(totalToday = total)
        }


    }
    fun updateReceiptImage(uri: Uri?) {
        _uiState.value = _uiState.value.copy(receiptImageUri = uri?.toString())
    }
}