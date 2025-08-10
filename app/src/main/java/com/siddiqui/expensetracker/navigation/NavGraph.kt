package com.siddiqui.expensetracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.siddiqui.expensetracker.data.repo.InMemoryExpenseRepository
import com.siddiqui.expensetracker.ui.entry.ExpenseEntryScreen
import com.siddiqui.expensetracker.ui.list.ExpenseListScreen
import com.siddiqui.expensetracker.ui.report.ExpenseReportScreen

sealed class Screen(val route:String){
    object Entry: Screen("entry")
    object List: Screen("list")
    object Report: Screen("report")
}


@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    val repo = InMemoryExpenseRepository

    NavHost(navController = navController, startDestination = Screen.Entry.route){
        composable(Screen.Entry.route) {
            ExpenseEntryScreen(navController,repo)
        }
        composable(Screen.List.route) { ExpenseListScreen(navController,repo) }
        composable(Screen.Report.route) { ExpenseReportScreen(navController,repo) }
    }

}