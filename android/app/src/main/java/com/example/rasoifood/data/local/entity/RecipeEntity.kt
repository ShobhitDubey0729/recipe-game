package com.example.rasoifood.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val slug: String,
    val description: String,
    val cuisine: String,
    val mealType: String,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val servings: Int,
    val finalCookedWeightG: Double?,
    val caloriesPer100g: Double,
    val proteinGPer100g: Double,
    val vegetarian: Boolean,
    val vegan: Boolean,
    val glutenFree: Boolean,
    val difficulty: String,
    val imageUrl: String?,
    val nutritionSource: String,
    val ingredientsJson: String,
    val instructionsJson: String,
    val tagsJson: String,
    val cachedAt: Long,
)
