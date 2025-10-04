package com.zooatlanta.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimalCategoryDao {
    @Query("SELECT * FROM animal_categories ORDER BY name")
    fun observeCategories(): Flow<List<AnimalCategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCategories(categories: List<AnimalCategoryEntity>)

    @Query("DELETE FROM animal_categories")
    suspend fun clearCategories()

    @Query("SELECT COUNT(*) FROM animal_categories")
    suspend fun countCategories(): Long
}
