package com.siddiqui.expensetracker.ui.report

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.siddiqui.expensetracker.factory.ExpenseReportViewModelFactory
import com.siddiqui.expensetracker.data.repo.ExpenseRepository
import com.siddiqui.expensetracker.vm.ExpenseReportViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseReportScreen(
    navController: NavController,
    repo: ExpenseRepository
) {
    val viewModel: ExpenseReportViewModel = viewModel(factory = ExpenseReportViewModelFactory(repo))
    val state by viewModel.uiState.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Expense Report") }) },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (state.isEmpty) {
                Text("No data available for the last 7 days.")
            } else {
                Text("Last 7 Days Daily Totals", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    state.dailyTotals.forEach { (date, total) ->
                        Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                            Text("₹${total.toInt()}")
                            Canvas(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(100.dp)
                            ) {
                                drawRect(
                                    color = Color.Blue,
                                    topLeft = Offset(
                                        0f,
                                        size.height - (total.toFloat() * 5).coerceAtMost(size.height)
                                    ),
                                    size = androidx.compose.ui.geometry.Size(
                                        width = size.width,
                                        height = (total.toFloat() * 5).coerceAtMost(size.height)
                                    )
                                )
                            }
                            Text(SimpleDateFormat("dd", Locale.getDefault()).format(Date(date)))
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text("Category-wise Totals", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                state.categoryTotals.forEach { (category, total) ->
                    Text("$category: ₹$total")
                }

                Spacer(Modifier.height(16.dp))

                Button(onClick = {
                    val csv = buildCsv(state.dailyTotals, state.categoryTotals)
                    saveCsvToInternal(navController.context, csv) { success, path ->
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                if (success) "CSV saved at: $path" else "Failed to save CSV"
                            )
                        }
                    }
                }) {
                    Text("Download CSV")
                }
            }

            Spacer(Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Back")
            }
        }
    }
}

fun buildCsv(
    dailyTotals: List<Pair<Long, Double>>,
    categoryTotals: Map<String, Double>
): String {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val sb = StringBuilder()
    sb.append("Date,Total\n")
    dailyTotals.filter { it.second > 0.0 }.forEach { (date, total) ->
        sb.append("${dateFormat.format(Date(date))},$total\n")
    }
    sb.append("\nCategory,Total\n")
    categoryTotals.forEach { (cat, total) ->
        sb.append("$cat,$total\n")
    }
    return sb.toString()
}

fun saveCsvToInternal(
    context: Context,
    csvContent: String,
    onResult: (Boolean, String?) -> Unit
) {
    try {
        val fileName = "expense_report_${System.currentTimeMillis()}.csv"
        val file = File(context.filesDir, fileName)
        file.writeText(csvContent)
        onResult(true, file.absolutePath)
    } catch (e: Exception) {
        e.printStackTrace()
        onResult(false, null)
    }
}