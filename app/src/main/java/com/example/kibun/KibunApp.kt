package com.example.kibun

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kibun.ui.screens.HomeScreen
import com.example.kibun.ui.screens.DiaryEntryScreen
import com.example.kibun.ui.viewmodel.KibunViewModel

@Composable
fun KibunApp(viewModel: KibunViewModel) {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "home"

    ) {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToDiary = { navController.navigate("diary_entry") }

            )
        }
        composable("diary_entry") {
            DiaryEntryScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

