package com.siddiqui.expensetracker.ui.entry

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.siddiqui.expensetracker.data.model.Category
import com.siddiqui.expensetracker.factory.ExpenseEntryViewModelFactory
import com.siddiqui.expensetracker.data.repo.ExpenseRepository
import com.siddiqui.expensetracker.vm.ExpenseEntryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar


@Composable
fun ExpenseEntryScreen(navController: NavController, repo: ExpenseRepository) {
    val viewModel: ExpenseEntryViewModel = viewModel(factory = ExpenseEntryViewModelFactory(repo))
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)
        .fillMaxSize()
        .padding(16.dp)) {

        Text(text = "Total Spent Today: ₹${state.totalToday}", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = state.title,
            onValueChange = viewModel::onTitleChange,
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = state.amount,
            onValueChange = viewModel::onAmountChange,
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        CategoryDropdown(selected = state.category, onCategorySelected = viewModel::onCategoryChange)

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = state.notes,
            onValueChange = viewModel::onNoteChange,
            label = { Text("Notes (optional)") },
            maxLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        val scope = rememberCoroutineScope()

        val imageLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            viewModel.updateReceiptImage(uri)
        }

        Button(onClick = {
            scope.launch {
                val isAdded = viewModel.submitExpense()
                Toast.makeText(
                    context,
                    if (isAdded) "Expense Added" else "Invalid or duplicate entry",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }) {
            Text("Add Expense")
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = { navController.navigate("list") }) {
            Text("View Expenses")
        }
    }
}

fun getYesterdayMillis(): Long {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, -1)
    return cal.timeInMillis
}

@Composable
fun CategoryDropdown(selected: Category, onCategorySelected: (Category) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selected.name)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Category.entries.forEach {
                DropdownMenuItem(
                    text = { Text(it.name) },
                    onClick = {
                        onCategorySelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}
