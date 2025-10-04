package com.zooatlanta.domain.usecase

import com.zooatlanta.domain.repository.AnimalRepository
import javax.inject.Inject

class RefreshCategoriesUseCase @Inject constructor(
    private val repository: AnimalRepository
) {
    suspend operator fun invoke() {
        repository.refreshCategories()
    }
}
