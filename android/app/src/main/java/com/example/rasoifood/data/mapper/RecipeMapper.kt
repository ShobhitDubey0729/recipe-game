package com.example.rasoifood.data.mapper

import com.example.rasoifood.data.local.entity.RecipeEntity
import com.example.rasoifood.data.remote.dto.IngredientDto
import com.example.rasoifood.data.remote.dto.RecipeDetailDto
import com.example.rasoifood.data.remote.dto.RecipeSummaryDto
import com.example.rasoifood.domain.model.Cuisine
import com.example.rasoifood.domain.model.Difficulty
import com.example.rasoifood.domain.model.Ingredient
import com.example.rasoifood.domain.model.MealType
import com.example.rasoifood.domain.model.RecipeDetail
import com.example.rasoifood.domain.model.RecipeSummary
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

fun RecipeSummaryDto.toSummary(): RecipeSummary = RecipeSummary(
    id = id,
    name = name,
    slug = slug,
    cuisine = cuisine.toCuisine(),
    mealType = mealType.toMealType(),
    prepTimeMinutes = prepTimeMinutes,
    cookTimeMinutes = cookTimeMinutes,
    caloriesPer100g = caloriesPer100g,
    proteinGPer100g = proteinGPer100g,
    vegetarian = vegetarian,
    vegan = vegan,
    glutenFree = glutenFree,
    difficulty = difficulty.toDifficulty(),
    imageUrl = imageUrl,
)

fun RecipeDetailDto.toDetail(): RecipeDetail = RecipeDetail(
    id = id,
    name = name,
    slug = slug,
    description = description,
    cuisine = cuisine.toCuisine(),
    mealType = mealType.toMealType(),
    prepTimeMinutes = prepTimeMinutes,
    cookTimeMinutes = cookTimeMinutes,
    servings = servings,
    finalCookedWeightG = finalCookedWeightG,
    caloriesPer100g = caloriesPer100g,
    proteinGPer100g = proteinGPer100g,
    vegetarian = vegetarian,
    vegan = vegan,
    glutenFree = glutenFree,
    difficulty = difficulty.toDifficulty(),
    imageUrl = imageUrl,
    nutritionSource = nutritionSource,
    ingredients = ingredients.map { it.toIngredient() },
    instructions = instructions,
    tags = tags,
)

fun RecipeDetailDto.toEntity(cachedAt: Long): RecipeEntity = RecipeEntity(
    id = id,
    name = name,
    slug = slug,
    description = description,
    cuisine = cuisine,
    mealType = mealType,
    prepTimeMinutes = prepTimeMinutes,
    cookTimeMinutes = cookTimeMinutes,
    servings = servings,
    finalCookedWeightG = finalCookedWeightG,
    caloriesPer100g = caloriesPer100g,
    proteinGPer100g = proteinGPer100g,
    vegetarian = vegetarian,
    vegan = vegan,
    glutenFree = glutenFree,
    difficulty = difficulty,
    imageUrl = imageUrl,
    nutritionSource = nutritionSource,
    ingredientsJson = json.encodeToString(ingredients),
    instructionsJson = json.encodeToString(instructions),
    tagsJson = json.encodeToString(tags),
    cachedAt = cachedAt,
)

fun RecipeSummaryDto.toEntity(cachedAt: Long): RecipeEntity = RecipeEntity(
    id = id,
    name = name,
    slug = slug,
    description = "",
    cuisine = cuisine,
    mealType = mealType,
    prepTimeMinutes = prepTimeMinutes,
    cookTimeMinutes = cookTimeMinutes,
    servings = 2,
    finalCookedWeightG = null,
    caloriesPer100g = caloriesPer100g,
    proteinGPer100g = proteinGPer100g,
    vegetarian = vegetarian,
    vegan = vegan,
    glutenFree = glutenFree,
    difficulty = difficulty,
    imageUrl = imageUrl,
    nutritionSource = "estimated",
    ingredientsJson = "[]",
    instructionsJson = "[]",
    tagsJson = "[]",
    cachedAt = cachedAt,
)

fun RecipeEntity.toSummary(): RecipeSummary = RecipeSummary(
    id = id,
    name = name,
    slug = slug,
    cuisine = cuisine.toCuisine(),
    mealType = mealType.toMealType(),
    prepTimeMinutes = prepTimeMinutes,
    cookTimeMinutes = cookTimeMinutes,
    caloriesPer100g = caloriesPer100g,
    proteinGPer100g = proteinGPer100g,
    vegetarian = vegetarian,
    vegan = vegan,
    glutenFree = glutenFree,
    difficulty = difficulty.toDifficulty(),
    imageUrl = imageUrl,
)

fun RecipeEntity.toDetail(): RecipeDetail = RecipeDetail(
    id = id,
    name = name,
    slug = slug,
    description = description,
    cuisine = cuisine.toCuisine(),
    mealType = mealType.toMealType(),
    prepTimeMinutes = prepTimeMinutes,
    cookTimeMinutes = cookTimeMinutes,
    servings = servings,
    finalCookedWeightG = finalCookedWeightG,
    caloriesPer100g = caloriesPer100g,
    proteinGPer100g = proteinGPer100g,
    vegetarian = vegetarian,
    vegan = vegan,
    glutenFree = glutenFree,
    difficulty = difficulty.toDifficulty(),
    imageUrl = imageUrl,
    nutritionSource = nutritionSource,
    ingredients = json.decodeFromString<List<IngredientDto>>(ingredientsJson).map { it.toIngredient() },
    instructions = json.decodeFromString<List<String>>(instructionsJson),
    tags = json.decodeFromString<List<String>>(tagsJson),
)

fun IngredientDto.toIngredient(): Ingredient = Ingredient(name = name, quantityG = quantityG)

fun Cuisine.toApiValue(): String = when (this) {
    Cuisine.NORTH_INDIAN -> "north_indian"
    Cuisine.SOUTH_INDIAN -> "south_indian"
}

fun MealType.toApiValue(): String = when (this) {
    MealType.BREAKFAST -> "breakfast"
    MealType.BRUNCH -> "brunch"
    MealType.LUNCH -> "lunch"
    MealType.SNACKS -> "snacks"
    MealType.DINNER -> "dinner"
}

fun String.toCuisine(): Cuisine = when (this) {
    "north_indian" -> Cuisine.NORTH_INDIAN
    "south_indian" -> Cuisine.SOUTH_INDIAN
    else -> Cuisine.NORTH_INDIAN
}

fun String.toMealType(): MealType = when (this) {
    "breakfast" -> MealType.BREAKFAST
    "brunch" -> MealType.BRUNCH
    "lunch" -> MealType.LUNCH
    "snacks" -> MealType.SNACKS
    "dinner" -> MealType.DINNER
    else -> MealType.LUNCH
}

fun String.toDifficulty(): Difficulty = when (this) {
    "easy" -> Difficulty.EASY
    "hard" -> Difficulty.HARD
    else -> Difficulty.MEDIUM
}

fun Cuisine.displayName(): String = when (this) {
    Cuisine.NORTH_INDIAN -> "North Indian"
    Cuisine.SOUTH_INDIAN -> "South Indian"
}

fun MealType.displayName(): String = name.lowercase().replaceFirstChar { it.titlecase() }
