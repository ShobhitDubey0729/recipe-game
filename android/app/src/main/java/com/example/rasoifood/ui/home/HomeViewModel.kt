package com.example.rasoifood.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasoifood.domain.model.RecipeSummary
import com.example.rasoifood.domain.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val popularRecipes: List<RecipeSummary> = emptyList(),
    val isOffline: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RecipeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val syncResult = repository.syncCatalog()
            val isOffline = syncResult.isFailure
            repository.observeRecipes().collect { recipes ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        popularRecipes = recipes.take(6),
                        isOffline = isOffline,
                        errorMessage = syncResult.exceptionOrNull()?.localizedMessage,
                    )
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.syncCatalog()
        }
    }
}
