package com.example.rasoifood.ui.recipes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasoifood.domain.model.Cuisine
import com.example.rasoifood.domain.model.MealType
import com.example.rasoifood.domain.model.RecipeFilters
import com.example.rasoifood.domain.model.RecipeSummary
import com.example.rasoifood.domain.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecipeListUiState(
    val isLoading: Boolean = true,
    val recipes: List<RecipeSummary> = emptyList(),
    val isOffline: Boolean = false,
    val errorMessage: String? = null,
    val filters: RecipeFilters = RecipeFilters(),
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RecipeListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: RecipeRepository,
) : ViewModel() {

    private val initialFilters = RecipeFilters(
        cuisine = savedStateHandle.get<String>("cuisine")?.let { parseCuisine(it) },
        mealType = savedStateHandle.get<String>("mealType")?.let { parseMealType(it) },
        searchQuery = savedStateHandle.get<String>("searchQuery"),
    )

    private val filtersFlow = MutableStateFlow(initialFilters)
    private val _uiState = MutableStateFlow(RecipeListUiState(filters = initialFilters))
    val uiState: StateFlow<RecipeListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            filtersFlow
                .flatMapLatest { filters -> repository.observeRecipes(filters) }
                .collect { recipes ->
                    _uiState.update { it.copy(recipes = recipes, isLoading = false) }
                }
        }
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = it.recipes.isEmpty(), errorMessage = null) }
            val filters = filtersFlow.value
            val result = repository.refreshRecipes(filters)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isOffline = result.isFailure,
                    errorMessage = result.exceptionOrNull()?.localizedMessage,
                )
            }
        }
    }

    fun updateFilters(filters: RecipeFilters) {
        filtersFlow.value = filters
        _uiState.update { it.copy(filters = filters) }
        refresh()
    }

    fun toggleVegetarian() {
        val current = filtersFlow.value
        updateFilters(current.copy(vegetarian = if (current.vegetarian == true) null else true))
    }

    fun toggleHighProtein() {
        val current = filtersFlow.value
        updateFilters(current.copy(highProtein = if (current.highProtein == true) null else true))
    }

    fun toggleLowCalorie() {
        val current = filtersFlow.value
        updateFilters(current.copy(lowCalorie = if (current.lowCalorie == true) null else true))
    }

    private fun parseCuisine(value: String): Cuisine? = when (value) {
        "north_indian", "NORTH_INDIAN" -> Cuisine.NORTH_INDIAN
        "south_indian", "SOUTH_INDIAN" -> Cuisine.SOUTH_INDIAN
        else -> null
    }

    private fun parseMealType(value: String): MealType? = when (value.lowercase()) {
        "breakfast" -> MealType.BREAKFAST
        "brunch" -> MealType.BRUNCH
        "lunch" -> MealType.LUNCH
        "snacks" -> MealType.SNACKS
        "dinner" -> MealType.DINNER
        else -> null
    }
}
