package com.example.rasoifood.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginatedRecipesDto(
    val items: List<RecipeSummaryDto>,
    val page: Int,
    @SerialName("page_size") val pageSize: Int,
    val total: Int,
)

@Serializable
data class RecipeSummaryDto(
    val id: String,
    val name: String,
    val slug: String,
    val cuisine: String,
    @SerialName("meal_type") val mealType: String,
    @SerialName("prep_time_minutes") val prepTimeMinutes: Int,
    @SerialName("cook_time_minutes") val cookTimeMinutes: Int,
    @SerialName("calories_per_100g") val caloriesPer100g: Double,
    @SerialName("protein_g_per_100g") val proteinGPer100g: Double,
    val vegetarian: Boolean,
    val vegan: Boolean,
    @SerialName("gluten_free") val glutenFree: Boolean,
    val difficulty: String,
    @SerialName("image_url") val imageUrl: String? = null,
)

@Serializable
data class RecipeDetailDto(
    val id: String,
    val name: String,
    val slug: String,
    val description: String,
    val cuisine: String,
    @SerialName("meal_type") val mealType: String,
    @SerialName("prep_time_minutes") val prepTimeMinutes: Int,
    @SerialName("cook_time_minutes") val cookTimeMinutes: Int,
    val servings: Int,
    @SerialName("final_cooked_weight_g") val finalCookedWeightG: Double? = null,
    @SerialName("calories_per_100g") val caloriesPer100g: Double,
    @SerialName("protein_g_per_100g") val proteinGPer100g: Double,
    val vegetarian: Boolean,
    val vegan: Boolean,
    @SerialName("gluten_free") val glutenFree: Boolean,
    val difficulty: String,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("nutrition_source") val nutritionSource: String,
    val ingredients: List<IngredientDto>,
    val instructions: List<String>,
    val tags: List<String>,
)

@Serializable
data class IngredientDto(
    val name: String,
    @SerialName("quantity_g") val quantityG: Double,
)
