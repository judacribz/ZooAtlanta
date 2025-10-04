package com.zooatlanta.domain.usecase

import com.zooatlanta.domain.model.AnimalCategory
import com.zooatlanta.domain.repository.AnimalRepository
import javax.inject.Inject

class RefreshAnimalsUseCase @Inject constructor(
    private val repository: AnimalRepository
) {
    suspend operator fun invoke(category: AnimalCategory?) {
        repository.refreshAnimals(category)
    }
}
