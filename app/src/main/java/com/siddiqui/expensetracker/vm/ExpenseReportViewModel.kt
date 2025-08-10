package com.siddiqui.expensetracker.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siddiqui.expensetracker.data.repo.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
data class ReportUiState(
    val dailyTotals: List<Pair<Long, Double>> = emptyList(),
    val categoryTotals: Map<String, Double> = emptyMap(),
    val isEmpty: Boolean = true
)

class ExpenseReportViewModel(private val repo: ExpenseRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState

    init {
        loadReportData()
    }

    private fun loadReportData() {
        viewModelScope.launch {
            repo.observeExpenses().collect { expenses ->
                val last7Days = getLast7Days()
                val dailyTotals = last7Days.map { date ->
                    val total = expenses.filter { isSameDay(it.dateMillis, date) }
                        .sumOf { it.amount }
                    date to total
                }
                val categoryTotals = expenses
                    .filter { it.dateMillis >= last7Days.first() }
                    .groupBy { it.category.name }
                    .mapValues { entry -> entry.value.sumOf { it.amount } }

                _uiState.value = ReportUiState(
                    dailyTotals = dailyTotals,
                    categoryTotals = categoryTotals,
                    isEmpty = expenses.isEmpty()
                )
            }
        }
    }

    companion object {
        fun getLast7Days(): List<Long> {
            val calendar = Calendar.getInstance()
            return (0..6).map {
                val date = calendar.timeInMillis
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                date
            }.reversed()
        }

        fun isSameDay(millis1: Long, millis2: Long): Boolean {
            val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            return sdf.format(Date(millis1)) == sdf.format(Date(millis2))
        }
    }
}

