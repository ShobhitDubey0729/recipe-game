package com.example.rasoifood.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rasoifood.ui.home.HomeScreen
import com.example.rasoifood.ui.navigation.Routes

@Composable
fun RasoiRoyaleAppRoot() {
    val navController = rememberNavController()

    Surface(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    onBrowseRecipes = { /* Phase 3 */ },
                    onStartGame = { /* Phase 6 */ },
                )
            }
        }
    }
}
