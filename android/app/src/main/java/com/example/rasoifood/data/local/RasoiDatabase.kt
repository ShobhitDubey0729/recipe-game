package com.example.rasoifood.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rasoifood.data.local.entity.RecipeEntity

@Database(
    entities = [RecipeEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class RasoiDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}
