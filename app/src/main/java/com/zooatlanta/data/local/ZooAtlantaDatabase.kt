package com.zooatlanta.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AnimalEntity::class, AnimalCategoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ZooAtlantaDatabase : RoomDatabase() {
    abstract fun animalDao(): AnimalDao
    abstract fun categoryDao(): AnimalCategoryDao
}
