package com.example.rasoifood.ui.recipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.rasoifood.data.mapper.displayName
import com.example.rasoifood.ui.common.EmptyContent
import com.example.rasoifood.ui.common.ErrorContent
import com.example.rasoifood.ui.common.LoadingContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    onBack: () -> Unit,
    viewModel: RecipeDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val recipe = uiState.recipe

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recipe?.name ?: "Recipe") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        when {
            uiState.isLoading && recipe == null -> LoadingContent(Modifier.padding(padding))
            recipe == null -> EmptyContent(
                message = uiState.errorMessage ?: "Recipe not found.",
                modifier = Modifier.padding(padding),
                onRetry = viewModel::refresh,
            )
            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (uiState.errorMessage != null) {
                    item {
                        ErrorContent(
                            message = "Showing cached recipe — ${uiState.errorMessage}",
                            onRetry = viewModel::refresh,
                        )
                    }
                }

                item {
                    AsyncImage(
                        model = recipe.imageUrl,
                        contentDescription = "${recipe.name} image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        contentScale = ContentScale.Crop,
                    )
                }

                item {
                    Text(recipe.name, style = MaterialTheme.typography.headlineMedium)
                    Text(
                        "${recipe.cuisine.displayName()} · ${recipe.mealType.displayName()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        "Prep: ${recipe.prepTimeMinutes} min · Cook: ${recipe.cookTimeMinutes} min · Serves: ${recipe.servings}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }

                item {
                    Text("Nutrition per 100 g (estimated)", style = MaterialTheme.typography.titleMedium)
                    Text("${recipe.caloriesPer100g.toInt()} kcal · ${recipe.proteinGPer100g}g protein")
                    Text(
                        "Source: ${recipe.nutritionSource}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                item {
                    Text("Ingredients", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    recipe.ingredients.forEach { ingredient ->
                        Text("• ${ingredient.name} — ${ingredient.quantityG}g")
                    }
                }

                item {
                    Divider()
                    Text("Instructions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }

                itemsIndexed(recipe.instructions) { index, step ->
                    Text("${index + 1}. $step")
                }
            }
        }
    }
}
