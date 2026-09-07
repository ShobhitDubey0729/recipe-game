package com.example.rasoifood.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rasoifood.ui.common.ErrorContent
import com.example.rasoifood.ui.common.LoadingContent
import com.example.rasoifood.ui.components.RecipeCard

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    onBrowseRecipes: () -> Unit,
    onSearch: (String) -> Unit,
    onMealCategoryClick: (String) -> Unit,
    onCuisineClick: (String) -> Unit,
    onRecipeClick: (String) -> Unit,
    onStartGame: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val mealCategories = listOf(
        "Breakfast" to "breakfast",
        "Brunch" to "brunch",
        "Lunch" to "lunch",
        "Snacks" to "snacks",
        "Dinner" to "dinner",
    )
    val cuisines = listOf(
        "North Indian" to "north_indian",
        "South Indian" to "south_indian",
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .semantics { contentDescription = "Home screen" },
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text(text = "Rasoi Royale", style = MaterialTheme.typography.headlineLarge)
            Text(
                text = "What are you cooking today?",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 4.dp),
            )
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search recipes") },
                singleLine = true,
            )
            Button(
                onClick = { onSearch(searchQuery.trim()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                enabled = searchQuery.isNotBlank(),
            ) {
                Text("Search")
            }
        }

        item {
            SectionTitle("Meal Categories")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                mealCategories.forEach { (label, apiValue) ->
                    AssistChip(
                        onClick = { onMealCategoryClick(apiValue) },
                        label = { Text(label) },
                    )
                }
            }
        }

        item {
            SectionTitle("Cuisine")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                cuisines.forEach { (label, apiValue) ->
                    AssistChip(
                        onClick = { onCuisineClick(apiValue) },
                        label = { Text(label) },
                    )
                }
            }
        }

        item {
            SectionTitle("Popular Recipes")
            if (uiState.isLoading) {
                LoadingContent(modifier = Modifier.height(120.dp))
            } else if (uiState.errorMessage != null && uiState.popularRecipes.isEmpty()) {
                ErrorContent(message = uiState.errorMessage ?: "Could not load recipes", onRetry = viewModel::refresh)
            } else if (uiState.isOffline && uiState.popularRecipes.isNotEmpty()) {
                Text(
                    text = "Offline — showing cached recipes",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        items(uiState.popularRecipes, key = { it.id }) { recipe ->
            RecipeCard(recipe = recipe, onClick = { onRecipeClick(recipe.id) })
        }

        item {
            Button(onClick = onBrowseRecipes, modifier = Modifier.fillMaxWidth()) {
                Text("Browse All Recipes")
            }
        }

        item {
            Button(onClick = onStartGame, modifier = Modifier.fillMaxWidth()) {
                Text("What Should We Cook?")
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 8.dp),
    )
}
