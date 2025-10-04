package com.zooatlanta.presentation.animals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.zooatlanta.core.coroutines.DispatcherProvider
import com.zooatlanta.domain.model.AnimalCategory
import com.zooatlanta.domain.usecase.ObserveAnimalsUseCase
import com.zooatlanta.domain.usecase.ObserveCategoriesUseCase
import com.zooatlanta.domain.usecase.RefreshAnimalsUseCase
import com.zooatlanta.domain.usecase.RefreshCategoriesUseCase
import com.zooatlanta.presentation.animals.model.toUiModel
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AnimalsViewModel @Inject constructor(
    private val observeAnimals: ObserveAnimalsUseCase,
    private val refreshAnimals: RefreshAnimalsUseCase,
    private val observeCategories: ObserveCategoriesUseCase,
    private val refreshCategories: RefreshCategoriesUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(AnimalsViewState(isLoading = true))
    val state: StateFlow<AnimalsViewState> = _state.asStateFlow()

    private val selectedCategoryFlow = MutableStateFlow<AnimalCategory?>(null)
    private val hasLoadedOnce = AtomicBoolean(false)

    init {
        observeCategories()
        observeAnimals()
        dispatch(AnimalsIntent.Load)
    }

    fun dispatch(intent: AnimalsIntent) {
        when (intent) {
            AnimalsIntent.Load -> loadInitial()
            AnimalsIntent.Refresh -> refreshCurrentCategory(isUserInitiated = true)
            AnimalsIntent.Retry -> refreshCurrentCategory(
                isUserInitiated = false,
                forceCategoryRefresh = true
            )
            is AnimalsIntent.SelectCategory -> selectCategory(intent.category)
        }
    }

    private fun observeCategories() {
        observeCategories.invoke()
            .onEach { categories ->
                val sorted = categories.sortedBy { it.name }
                updateState {
                    it.copy(
                        categories = sorted,
                        errorMessage = null
                    )
                }
                val currentSelection = selectedCategoryFlow.value
                val currentSlug = currentSelection?.slug
                if (currentSlug != null) {
                    val updatedSelection = sorted.firstOrNull { it.slug == currentSlug }
                    if (updatedSelection == null) {
                        selectedCategoryFlow.value = null
                        updateState { state -> state.copy(selectedCategory = null) }
                    } else if (
                        updatedSelection.name != currentSelection.name ||
                        updatedSelection.url != currentSelection.url
                    ) {
                        selectedCategoryFlow.value = updatedSelection
                        updateState { state -> state.copy(selectedCategory = updatedSelection) }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeAnimals() {
        selectedCategoryFlow
            .flatMapLatest { category ->
                observeAnimals.invoke(category)
                    .map { animals -> animals.map { it.toUiModel() } to category }
            }
            .onEach { (animals, category) ->
                updateState {
                    it.copy(
                        animals = animals,
                        selectedCategory = category,
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = null
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadInitial() {
        if (hasLoadedOnce.compareAndSet(false, true)) {
            viewModelScope.launch(dispatcherProvider.io) {
                updateState { it.copy(isLoading = true, errorMessage = null) }
                try {
                    refreshCategories.invoke()
                    refreshAnimals.invoke(null)
                    updateState { it.copy(isLoading = false, errorMessage = null) }
                } catch (exception: Exception) {
                    if (exception is CancellationException) throw exception
                    updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Unable to load animals"
                        )
                    }
                }
            }
        }
    }

    private fun refreshCurrentCategory(
        isUserInitiated: Boolean,
        forceCategoryRefresh: Boolean = false
    ) {
        viewModelScope.launch(dispatcherProvider.io) {
            val shouldRefreshCategories = forceCategoryRefresh || _state.value.categories.isEmpty()
            if (isUserInitiated) {
                updateState { it.copy(isRefreshing = true, errorMessage = null) }
            } else {
                updateState { it.copy(isLoading = true, errorMessage = null) }
            }
            try {
                if (shouldRefreshCategories) {
                    refreshCategories.invoke()
                }
                refreshAnimals.invoke(selectedCategoryFlow.value)
                updateState {
                    it.copy(
                        isRefreshing = false,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (exception: Exception) {
                if (exception is CancellationException) throw exception
                val fallbackMessage = if (shouldRefreshCategories) {
                    "Unable to load animals"
                } else {
                    "Unable to refresh animals"
                }
                updateState {
                    it.copy(
                        isRefreshing = false,
                        isLoading = false,
                        errorMessage = exception.message ?: fallbackMessage
                    )
                }
            }
        }
    }

    private fun selectCategory(category: AnimalCategory?) {
        val currentSlug = selectedCategoryFlow.value?.slug
        val nextSlug = category?.slug
        if (currentSlug == nextSlug && _state.value.errorMessage == null) return
        selectedCategoryFlow.value = category
        updateState {
            it.copy(
                selectedCategory = category,
                errorMessage = null
            )
        }
        refreshCurrentCategory(isUserInitiated = false)
    }

    private fun updateState(reducer: (AnimalsViewState) -> AnimalsViewState) {
        _state.update(reducer)
    }

    class Factory @Inject constructor(
        private val provider: javax.inject.Provider<AnimalsViewModel>
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AnimalsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return provider.get() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${'$'}modelClass")
        }
    }
}
