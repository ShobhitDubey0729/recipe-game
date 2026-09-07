package com.example.rasoifood.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rasoifood.domain.model.RecipeFilters
import com.example.rasoifood.domain.model.RecipeSummary
import com.example.rasoifood.domain.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val recipes: List<RecipeSummary> = emptyList(),
    val isOffline: Boolean = false,
    val errorMessage: String? = null,
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: RecipeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val queryFlow = MutableStateFlow("")

    init {
        viewModelScope.launch {
            queryFlow
                .debounce(300)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        flowOf(emptyList())
                    } else {
                        flow {
                            val result = repository.refreshRecipes(RecipeFilters(searchQuery = query))
                            _uiState.update {
                                it.copy(
                                    isOffline = result.isFailure,
                                    errorMessage = result.exceptionOrNull()?.localizedMessage,
                                )
                            }
                            repository.observeRecipes(RecipeFilters(searchQuery = query))
                                .collect { recipes -> emit(recipes) }
                        }
                    }
                }
                .collect { recipes ->
                    _uiState.update { it.copy(isLoading = false, recipes = recipes) }
                }
        }
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query, isLoading = query.isNotBlank()) }
        queryFlow.value = query
    }
}
