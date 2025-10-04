package com.zooatlanta.domain.usecase

import com.zooatlanta.domain.model.Animal
import com.zooatlanta.domain.model.AnimalCategory
import com.zooatlanta.domain.repository.AnimalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAnimalsUseCase @Inject constructor(
    private val repository: AnimalRepository
) {
    operator fun invoke(category: AnimalCategory?): Flow<List<Animal>> =
        repository.observeAnimals(category)
}
