package com.example.rasoifood.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    onBrowseRecipes: () -> Unit,
    onStartGame: () -> Unit,
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val mealCategories = listOf("Breakfast", "Brunch", "Lunch", "Snacks", "Dinner")
    val cuisines = listOf("North Indian", "South Indian")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .semantics { contentDescription = "Home screen" },
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text(
                text = "Rasoi Royale",
                style = MaterialTheme.typography.headlineLarge,
            )
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
        }

        item {
            SectionTitle("Meal Categories")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                mealCategories.forEach { category ->
                    AssistChip(onClick = onBrowseRecipes, label = { Text(category) })
                }
            }
        }

        item {
            SectionTitle("Cuisine")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                cuisines.forEach { cuisine ->
                    AssistChip(onClick = onBrowseRecipes, label = { Text(cuisine) })
                }
            }
        }

        item {
            SectionTitle("Popular Recipes")
            Text(
                text = "Recipe catalog loads in Phase 3. Browse North & South Indian dishes with nutrition estimates.",
                style = MaterialTheme.typography.bodyMedium,
            )
            Button(
                onClick = onBrowseRecipes,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                Text("Browse Recipes")
            }
        }

        item {
            Button(
                onClick = onStartGame,
                modifier = Modifier.fillMaxWidth(),
            ) {
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
