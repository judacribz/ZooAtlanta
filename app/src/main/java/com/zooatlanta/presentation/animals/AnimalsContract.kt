package com.zooatlanta.presentation.animals

import com.zooatlanta.domain.model.AnimalCategory
import com.zooatlanta.presentation.animals.model.AnimalUiModel

sealed class AnimalsIntent {
    data object Load : AnimalsIntent()
    data class SelectCategory(val category: AnimalCategory?) : AnimalsIntent()
    data object Retry : AnimalsIntent()
    data object Refresh : AnimalsIntent()
}

data class AnimalsViewState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val animals: List<AnimalUiModel> = emptyList(),
    val categories: List<AnimalCategory> = emptyList(),
    val selectedCategory: AnimalCategory? = null,
    val errorMessage: String? = null
)
