package com.example.rasoifood.ui.recipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rasoifood.data.mapper.displayName
import com.example.rasoifood.ui.common.EmptyContent
import com.example.rasoifood.ui.common.ErrorContent
import com.example.rasoifood.ui.common.LoadingContent
import com.example.rasoifood.ui.components.RecipeCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RecipeListScreen(
    onBack: () -> Unit,
    onRecipeClick: (String) -> Unit,
    viewModel: RecipeListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val title = buildListTitle(uiState.filters)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    selected = uiState.filters.vegetarian == true,
                    onClick = viewModel::toggleVegetarian,
                    label = { Text("Vegetarian") },
                )
                FilterChip(
                    selected = uiState.filters.highProtein == true,
                    onClick = viewModel::toggleHighProtein,
                    label = { Text("High protein") },
                )
                FilterChip(
                    selected = uiState.filters.lowCalorie == true,
                    onClick = viewModel::toggleLowCalorie,
                    label = { Text("Low calorie") },
                )
            }

            if (uiState.errorMessage != null && uiState.recipes.isNotEmpty()) {
                ErrorContent(
                    message = "Offline mode — showing cached recipes",
                    onRetry = viewModel::refresh,
                )
            }

            when {
                uiState.isLoading && uiState.recipes.isEmpty() -> LoadingContent()
                uiState.recipes.isEmpty() -> EmptyContent(
                    message = if (uiState.errorMessage != null) {
                        "Could not load recipes. Check your connection."
                    } else {
                        "No recipes match your filters."
                    },
                    onRetry = viewModel::refresh,
                )
                else -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(uiState.recipes, key = { it.id }) { recipe ->
                        RecipeCard(recipe = recipe, onClick = { onRecipeClick(recipe.id) })
                    }
                }
            }
        }
    }
}

private fun buildListTitle(filters: com.example.rasoifood.domain.model.RecipeFilters): String {
    val parts = mutableListOf<String>()
    filters.cuisine?.let { parts.add(it.displayName()) }
    filters.mealType?.let { parts.add(it.displayName()) }
    return if (parts.isEmpty()) "All Recipes" else parts.joinToString(" · ")
}
