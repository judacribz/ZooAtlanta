package com.zooatlanta.di

import android.app.Application
import androidx.room.Room
import com.squareup.anvil.annotations.ContributesTo
import com.zooatlanta.data.local.AnimalCategoryDao
import com.zooatlanta.data.local.AnimalDao
import com.zooatlanta.data.local.ZooAtlantaDatabase
import dagger.Module
import dagger.Provides

@Module
@ContributesTo(AppScope::class)
object DatabaseModule {

    @Provides
    @SingleInAppScope
    fun provideDatabase(application: Application): ZooAtlantaDatabase =
        Room.databaseBuilder(
            application,
            ZooAtlantaDatabase::class.java,
            "zoo_atlanta.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideAnimalDao(database: ZooAtlantaDatabase): AnimalDao = database.animalDao()

    @Provides
    fun provideCategoryDao(database: ZooAtlantaDatabase): AnimalCategoryDao = database.categoryDao()
}
