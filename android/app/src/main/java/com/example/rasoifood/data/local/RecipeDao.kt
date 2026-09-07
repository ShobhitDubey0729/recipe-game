package com.example.rasoifood.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rasoifood.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes ORDER BY name ASC")
    fun observeAll(): Flow<List<RecipeEntity>>

    @Query(
        """
        SELECT * FROM recipes
        WHERE (:cuisine IS NULL OR cuisine = :cuisine)
          AND (:mealType IS NULL OR mealType = :mealType)
          AND (:vegetarian IS NULL OR vegetarian = :vegetarian)
          AND (:vegan IS NULL OR vegan = :vegan)
          AND (:glutenFree IS NULL OR glutenFree = :glutenFree)
          AND (:highProtein IS NULL OR proteinGPer100g >= 8.0)
          AND (:lowCalorie IS NULL OR caloriesPer100g <= 200.0)
          AND (:maxPrepTime IS NULL OR prepTimeMinutes <= :maxPrepTime)
          AND (
            :searchQuery IS NULL OR :searchQuery = '' OR
            name LIKE '%' || :searchQuery || '%' OR
            description LIKE '%' || :searchQuery || '%' OR
            tagsJson LIKE '%' || :searchQuery || '%'
          )
        ORDER BY name ASC
        """,
    )
    fun observeFiltered(
        cuisine: String?,
        mealType: String?,
        vegetarian: Boolean?,
        vegan: Boolean?,
        glutenFree: Boolean?,
        highProtein: Boolean?,
        lowCalorie: Boolean?,
        maxPrepTime: Int?,
        searchQuery: String?,
    ): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    fun observeById(id: String): Flow<RecipeEntity?>

    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): RecipeEntity?

    @Query("SELECT COUNT(*) FROM recipes")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipes: List<RecipeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: RecipeEntity)
}
