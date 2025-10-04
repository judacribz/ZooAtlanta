package com.zooatlanta.data.remote

data class AnimalDto(
    val name: String,
    val scientificName: String?,
    val imageUrl: String?,
    val diet: String?,
    val habitatRange: String?,
    val conservationStatus: String?,
    val profileUrl: String?,
    val categoryName: String?,
    val categorySlug: String?
)
