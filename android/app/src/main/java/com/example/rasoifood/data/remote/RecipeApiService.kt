package com.example.rasoifood.data.remote

import com.example.rasoifood.data.remote.dto.PaginatedRecipesDto
import com.example.rasoifood.data.remote.dto.RecipeDetailDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {

    @GET("api/v1/recipes")
    suspend fun getRecipes(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("cuisine") cuisine: String? = null,
        @Query("meal_type") mealType: String? = null,
        @Query("vegetarian") vegetarian: Boolean? = null,
        @Query("vegan") vegan: Boolean? = null,
        @Query("gluten_free") glutenFree: Boolean? = null,
        @Query("high_protein") highProtein: Boolean? = null,
        @Query("low_calorie") lowCalorie: Boolean? = null,
        @Query("max_prep_time_minutes") maxPrepTimeMinutes: Int? = null,
        @Query("sort") sort: String? = null,
    ): PaginatedRecipesDto

    @GET("api/v1/recipes/search")
    suspend fun searchRecipes(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("cuisine") cuisine: String? = null,
        @Query("meal_type") mealType: String? = null,
    ): PaginatedRecipesDto

    @GET("api/v1/recipes/{id}")
    suspend fun getRecipe(@Path("id") id: String): RecipeDetailDto
}
