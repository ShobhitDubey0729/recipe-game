package com.example.rasoifood.domain.model

enum class Cuisine {
    NORTH_INDIAN,
    SOUTH_INDIAN,
}

enum class MealType {
    BREAKFAST,
    BRUNCH,
    LUNCH,
    SNACKS,
    DINNER,
}

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD,
}

data class Ingredient(
    val name: String,
    val quantityG: Double,
)

data class RecipeSummary(
    val id: String,
    val name: String,
    val slug: String,
    val cuisine: Cuisine,
    val mealType: MealType,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val caloriesPer100g: Double,
    val proteinGPer100g: Double,
    val vegetarian: Boolean,
    val vegan: Boolean,
    val glutenFree: Boolean,
    val difficulty: Difficulty,
    val imageUrl: String?,
)

data class RecipeDetail(
    val id: String,
    val name: String,
    val slug: String,
    val description: String,
    val cuisine: Cuisine,
    val mealType: MealType,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val servings: Int,
    val finalCookedWeightG: Double?,
    val caloriesPer100g: Double,
    val proteinGPer100g: Double,
    val vegetarian: Boolean,
    val vegan: Boolean,
    val glutenFree: Boolean,
    val difficulty: Difficulty,
    val imageUrl: String?,
    val nutritionSource: String,
    val ingredients: List<Ingredient>,
    val instructions: List<String>,
    val tags: List<String>,
)

data class RecipeFilters(
    val cuisine: Cuisine? = null,
    val mealType: MealType? = null,
    val vegetarian: Boolean? = null,
    val vegan: Boolean? = null,
    val glutenFree: Boolean? = null,
    val highProtein: Boolean? = null,
    val lowCalorie: Boolean? = null,
    val maxPrepTimeMinutes: Int? = null,
    val searchQuery: String? = null,
)
