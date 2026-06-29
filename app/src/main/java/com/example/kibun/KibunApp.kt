package com.example.kibun

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kibun.ui.components.CharacterOverlay
import com.example.kibun.ui.screens.HomeScreen
import com.example.kibun.ui.screens.DiaryEntryScreen
import com.example.kibun.ui.screens.SettingsScreen
import com.example.kibun.ui.viewmodel.KibunViewModel

@Composable
fun KibunApp(viewModel: KibunViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToDiary = { navController.navigate("diary_entry") },
                    onNavigateToSettings = { navController.navigate("settings") }
                )
            }
            composable("diary_entry") {
                DiaryEntryScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable("settings") {
                SettingsScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        // 全画面共通のキャラクターオーバーレイ
        CharacterOverlay(currentRoute = currentRoute)
    }
}

