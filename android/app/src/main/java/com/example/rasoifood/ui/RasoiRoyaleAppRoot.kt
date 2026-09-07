package com.example.rasoifood.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rasoifood.ui.home.HomeScreen
import com.example.rasoifood.ui.navigation.Routes
import com.example.rasoifood.ui.recipes.RecipeDetailScreen
import com.example.rasoifood.ui.recipes.RecipeListScreen
import com.example.rasoifood.ui.search.SearchScreen

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
                    onBrowseRecipes = { navController.navigate(Routes.recipes()) },
                    onSearch = { query -> navController.navigate(Routes.search(query)) },
                    onMealCategoryClick = { mealType ->
                        navController.navigate(Routes.recipes(mealType = mealType))
                    },
                    onCuisineClick = { cuisine ->
                        navController.navigate(Routes.recipes(cuisine = cuisine))
                    },
                    onRecipeClick = { id -> navController.navigate(Routes.recipeDetail(id)) },
                    onStartGame = { /* Phase 6 */ },
                )
            }

            composable(
                route = "${Routes.RECIPES}?cuisine={cuisine}&mealType={mealType}",
                arguments = listOf(
                    navArgument("cuisine") { type = NavType.StringType; defaultValue = "" },
                    navArgument("mealType") { type = NavType.StringType; defaultValue = "" },
                ),
            ) {
                RecipeListScreen(
                    onBack = { navController.popBackStack() },
                    onRecipeClick = { id -> navController.navigate(Routes.recipeDetail(id)) },
                )
            }

            composable(
                route = "${Routes.RECIPE_DETAIL}/{recipeId}",
                arguments = listOf(navArgument("recipeId") { type = NavType.StringType }),
            ) {
                RecipeDetailScreen(onBack = { navController.popBackStack() })
            }

            composable(
                route = "${Routes.SEARCH}?query={query}",
                arguments = listOf(navArgument("query") { type = NavType.StringType; defaultValue = "" }),
            ) { entry ->
                val query = entry.arguments?.getString("query") ?: ""
                SearchScreen(
                    initialQuery = query,
                    onBack = { navController.popBackStack() },
                    onRecipeClick = { id -> navController.navigate(Routes.recipeDetail(id)) },
                )
            }
        }
    }
}
