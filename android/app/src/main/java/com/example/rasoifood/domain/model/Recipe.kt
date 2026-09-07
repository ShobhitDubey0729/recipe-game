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

data class Recipe(
    val id: String,
    val name: String,
    val cuisine: Cuisine,
    val mealType: MealType,
    val preparationTimeMinutes: Int,
    val caloriesPer100g: Double,
    val proteinGPer100g: Double,
    val imageUrl: String?,
)
