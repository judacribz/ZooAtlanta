package com.zooatlanta.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimalDao {
    @Query("SELECT * FROM animals WHERE (:categorySlug IS NULL OR category_slug = :categorySlug) ORDER BY name")
    fun observeAnimals(categorySlug: String?): Flow<List<AnimalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAnimals(animals: List<AnimalEntity>)

    @Query("DELETE FROM animals WHERE (:categorySlug IS NULL OR category_slug = :categorySlug)")
    suspend fun deleteAnimalsForCategory(categorySlug: String?)

    @Query("DELETE FROM animals")
    suspend fun clearAnimals()

    @Query("SELECT COUNT(*) FROM animals")
    suspend fun countAnimals(): Long
}
