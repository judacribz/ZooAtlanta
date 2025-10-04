package com.zooatlanta.domain.model

data class Animal(
    val id: Long,
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
