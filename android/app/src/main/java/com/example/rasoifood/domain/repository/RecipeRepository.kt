package com.example.rasoifood.domain.repository

import com.example.rasoifood.domain.model.RecipeDetail
import com.example.rasoifood.domain.model.RecipeFilters
import com.example.rasoifood.domain.model.RecipeSummary
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun observeRecipes(filters: RecipeFilters = RecipeFilters()): Flow<List<RecipeSummary>>
    fun observeRecipeDetail(id: String): Flow<RecipeDetail?>
    suspend fun refreshRecipes(filters: RecipeFilters = RecipeFilters()): Result<Int>
    suspend fun refreshRecipeDetail(id: String): Result<RecipeDetail>
    suspend fun syncCatalog(): Result<Int>
}
