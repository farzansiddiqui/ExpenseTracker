package com.siddiqui.expensetracker.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.siddiqui.expensetracker.data.model.Expense
import com.siddiqui.expensetracker.factory.ExpenseListViewModelFactory
import com.siddiqui.expensetracker.data.repo.ExpenseRepository
import com.siddiqui.expensetracker.vm.ExpenseListViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    navController: NavController,
    repo: ExpenseRepository
) {
    val viewModel: ExpenseListViewModel = viewModel(factory = ExpenseListViewModelFactory(repo))
    val expenses by viewModel.expenses.collectAsState()

    var selectedDate by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var groupByCategory by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expenses List") },
                actions = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                    }
                    TextButton(onClick = { navController.navigate("report") }) {
                        Text("Report", color = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Selected Date: ${SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(
                    Date(
                        selectedDate
                    )
                )}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                FilterChip(
                    selected = groupByCategory,
                    onClick = { groupByCategory = true },
                    label = { Text("Group by Category") }
                )
                Spacer(Modifier.width(8.dp))
                FilterChip(
                    selected = !groupByCategory,
                    onClick = { groupByCategory = false },
                    label = { Text("Group by Time") }
                )
            }

            Spacer(Modifier.height(8.dp))

            Text("Total Count: ${expenses.size}")
            Text("Total Amount: ₹${expenses.sumOf { it.amount }}")
            Spacer(Modifier.height(8.dp))

            if (expenses.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No expenses found for this date.")
                }
            } else {
                val grouped = if (groupByCategory) {
                    expenses.groupBy { it.category.name }
                } else {
                    expenses.groupBy { expense ->
                        val hour = Calendar.getInstance().apply {
                            timeInMillis = expense.dateMillis
                        }.get(Calendar.HOUR_OF_DAY)
                        when (hour) {
                            in 0..11 -> "Morning"
                            in 12..17 -> "Afternoon"
                            else -> "Evening"
                        }
                    }
                }

                LazyColumn {
                    grouped.forEach { (group, items) ->
                        item {
                            Text(
                                text = group,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                        items(items) { expense ->
                            ExpenseListItem(expense)
                        }
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate)

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    selectedDate = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                    viewModel.loadExpensesForDate(selectedDate)
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = true
            )
        }
    }
}

@Composable
fun ExpenseListItem(expense: Expense) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(expense.title, style = MaterialTheme.typography.titleSmall)
            Text("₹${expense.amount}", style = MaterialTheme.typography.bodyMedium)
            Text("Category: ${expense.category}", style = MaterialTheme.typography.bodySmall)
            expense.notes?.let { Text("Notes: $it", style = MaterialTheme.typography.bodySmall) }
        }
    }
}