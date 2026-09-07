package com.example.rasoifood.ui.recipes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasoifood.domain.model.RecipeDetail
import com.example.rasoifood.domain.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecipeDetailUiState(
    val isLoading: Boolean = true,
    val recipe: RecipeDetail? = null,
    val isOffline: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: RecipeRepository,
) : ViewModel() {

    private val recipeId: String = savedStateHandle["recipeId"] ?: ""

    private val _uiState = MutableStateFlow(RecipeDetailUiState())
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()

    init {
        observeDetail()
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = it.recipe == null, errorMessage = null) }
            val result = repository.refreshRecipeDetail(recipeId)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isOffline = result.isFailure,
                    errorMessage = result.exceptionOrNull()?.message,
                    recipe = result.getOrNull() ?: it.recipe,
                )
            }
        }
    }

    private fun observeDetail() {
        viewModelScope.launch {
            repository.observeRecipeDetail(recipeId).collect { recipe ->
                _uiState.update {
                    it.copy(recipe = recipe, isLoading = recipe == null && it.isLoading)
                }
            }
        }
    }
}
