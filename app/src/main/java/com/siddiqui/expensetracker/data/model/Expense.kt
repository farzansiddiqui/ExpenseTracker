package com.siddiqui.expensetracker.data.model

data class Expense(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val amount: Double,
    val category: Category,
    val notes: String? = null,
    val dateMillis: Long = System.currentTimeMillis(),
    val receiptUri: String? = null
)

enum class Category {
    Staff, Travel, Food, Utility
}
