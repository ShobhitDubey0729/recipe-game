package com.example.rasoifood.data.mapper

import com.example.rasoifood.domain.model.Cuisine
import com.example.rasoifood.domain.model.MealType
import org.junit.Assert.assertEquals
import org.junit.Test

class RecipeMapperTest {

    @Test
    fun cuisineApiMapping() {
        assertEquals("north_indian", Cuisine.NORTH_INDIAN.toApiValue())
        assertEquals(Cuisine.SOUTH_INDIAN, "south_indian".toCuisine())
    }

    @Test
    fun mealTypeApiMapping() {
        assertEquals("breakfast", MealType.BREAKFAST.toApiValue())
        assertEquals(MealType.DINNER, "dinner".toMealType())
    }
}
