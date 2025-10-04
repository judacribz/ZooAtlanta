package com.zooatlanta.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animals")
data class AnimalEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "scientific_name") val scientificName: String?,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "diet") val diet: String?,
    @ColumnInfo(name = "habitat_range") val habitatRange: String?,
    @ColumnInfo(name = "conservation_status") val conservationStatus: String?,
    @ColumnInfo(name = "profile_url") val profileUrl: String?,
    @ColumnInfo(name = "category_name") val categoryName: String?,
    @ColumnInfo(name = "category_slug") val categorySlug: String?
)
