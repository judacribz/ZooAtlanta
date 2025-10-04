package com.zooatlanta.data.repository

import androidx.room.withTransaction
import com.squareup.anvil.annotations.ContributesBinding
import com.zooatlanta.data.local.AnimalCategoryDao
import com.zooatlanta.data.local.AnimalDao
import com.zooatlanta.data.local.ZooAtlantaDatabase
import com.zooatlanta.data.mapper.toDomain
import com.zooatlanta.data.mapper.toEntity
import com.zooatlanta.data.remote.AnimalCategoryDto
import com.zooatlanta.data.remote.ZooAtlantaScraper
import com.zooatlanta.di.AppScope
import com.zooatlanta.di.SingleInAppScope
import com.zooatlanta.domain.model.Animal
import com.zooatlanta.domain.model.AnimalCategory
import com.zooatlanta.domain.repository.AnimalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ContributesBinding(AppScope::class, AnimalRepository::class)
@SingleInAppScope
class AnimalRepositoryImpl @Inject constructor(
    private val animalDao: AnimalDao,
    private val categoryDao: AnimalCategoryDao,
    private val scraper: ZooAtlantaScraper,
    private val database: ZooAtlantaDatabase
) : AnimalRepository {

    override fun observeAnimals(category: AnimalCategory?): Flow<List<Animal>> {
        val slug = category?.slug
        return animalDao.observeAnimals(slug).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeCategories(): Flow<List<AnimalCategory>> =
        categoryDao.observeCategories().map { categories ->
            categories.map { it.toDomain() }
        }

    override suspend fun refreshAnimals(category: AnimalCategory?) {
        val dtoCategory = category?.let { AnimalCategoryDto(it.name, it.slug, it.url) }
        val animals = scraper.fetchAnimals(dtoCategory).map { it.toEntity() }
        database.withTransaction {
            if (category == null) {
                animalDao.clearAnimals()
            } else {
                animalDao.deleteAnimalsForCategory(category.slug)
            }
            animalDao.upsertAnimals(animals)
        }
    }

    override suspend fun refreshCategories() {
        val categories = scraper.fetchCategories().map { it.toEntity() }
        database.withTransaction {
            categoryDao.clearCategories()
            categoryDao.upsertCategories(categories)
        }
    }
}
