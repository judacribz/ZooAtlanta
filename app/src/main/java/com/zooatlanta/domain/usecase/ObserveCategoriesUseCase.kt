package com.zooatlanta.domain.usecase

import com.zooatlanta.domain.model.AnimalCategory
import com.zooatlanta.domain.repository.AnimalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCategoriesUseCase @Inject constructor(
    private val repository: AnimalRepository
) {
    operator fun invoke(): Flow<List<AnimalCategory>> = repository.observeCategories()
}
