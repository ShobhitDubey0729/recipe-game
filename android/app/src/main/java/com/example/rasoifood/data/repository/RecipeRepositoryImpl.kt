package com.example.rasoifood.data.repository

import com.example.rasoifood.data.local.RecipeDao
import com.example.rasoifood.data.mapper.toApiValue
import com.example.rasoifood.data.mapper.toDetail
import com.example.rasoifood.data.mapper.toEntity
import com.example.rasoifood.data.mapper.toSummary
import com.example.rasoifood.data.remote.RecipeApiService
import com.example.rasoifood.domain.model.RecipeDetail
import com.example.rasoifood.domain.model.RecipeFilters
import com.example.rasoifood.domain.model.RecipeSummary
import com.example.rasoifood.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepositoryImpl @Inject constructor(
    private val api: RecipeApiService,
    private val dao: RecipeDao,
) : RecipeRepository {

    override fun observeRecipes(filters: RecipeFilters): Flow<List<RecipeSummary>> {
        val search = filters.searchQuery?.trim()?.takeIf { it.isNotEmpty() }
        return dao.observeFiltered(
            cuisine = filters.cuisine?.toApiValue(),
            mealType = filters.mealType?.toApiValue(),
            vegetarian = filters.vegetarian,
            vegan = filters.vegan,
            glutenFree = filters.glutenFree,
            highProtein = filters.highProtein,
            lowCalorie = filters.lowCalorie,
            maxPrepTime = filters.maxPrepTimeMinutes,
            searchQuery = search,
        ).map { entities -> entities.map { it.toSummary() } }
    }

    override fun observeRecipeDetail(id: String): Flow<RecipeDetail?> =
        dao.observeById(id).map { entity -> entity?.toDetail() }

    override suspend fun refreshRecipes(filters: RecipeFilters): Result<Int> = runCatching {
        val search = filters.searchQuery?.trim()?.takeIf { it.isNotEmpty() }
        val response = if (search != null) {
            api.searchRecipes(
                query = search,
                page = 1,
                pageSize = 100,
                cuisine = filters.cuisine?.toApiValue(),
                mealType = filters.mealType?.toApiValue(),
            )
        } else {
            api.getRecipes(
                page = 1,
                pageSize = 100,
                cuisine = filters.cuisine?.toApiValue(),
                mealType = filters.mealType?.toApiValue(),
                vegetarian = filters.vegetarian,
                vegan = filters.vegan,
                glutenFree = filters.glutenFree,
                highProtein = filters.highProtein,
                lowCalorie = filters.lowCalorie,
                maxPrepTimeMinutes = filters.maxPrepTimeMinutes,
            )
        }
        val now = System.currentTimeMillis()
        dao.insertAll(response.items.map { it.toEntity(now) })
        response.items.size
    }

    override suspend fun refreshRecipeDetail(id: String): Result<RecipeDetail> = runCatching {
        val detail = api.getRecipe(id)
        dao.insert(detail.toEntity(System.currentTimeMillis()))
        detail.toDetail()
    }

    override suspend fun syncCatalog(): Result<Int> = runCatching {
        var page = 1
        var totalInserted = 0
        var total = Int.MAX_VALUE
        val now = System.currentTimeMillis()

        while ((page - 1) * PAGE_SIZE < total) {
            val response = api.getRecipes(page = page, pageSize = PAGE_SIZE)
            total = response.total
            if (response.items.isEmpty()) break
            dao.insertAll(response.items.map { it.toEntity(now) })
            totalInserted += response.items.size
            page++
        }
        totalInserted
    }

    private companion object {
        const val PAGE_SIZE = 50
    }
}
