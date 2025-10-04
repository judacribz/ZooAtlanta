package com.zooatlanta.domain.repository

import com.zooatlanta.domain.model.Animal
import com.zooatlanta.domain.model.AnimalCategory
import kotlinx.coroutines.flow.Flow

interface AnimalRepository {
    fun observeAnimals(category: AnimalCategory?): Flow<List<Animal>>
    fun observeCategories(): Flow<List<AnimalCategory>>
    suspend fun refreshAnimals(category: AnimalCategory?)
    suspend fun refreshCategories()
}
