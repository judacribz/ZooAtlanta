package com.zooatlanta.presentation.animals.model

data class AnimalUiModel(
    val id: Long,
    val name: String,
    val scientificName: String?,
    val imageUrl: String?,
    val diet: String?,
    val habitatRange: String?,
    val conservationStatus: String?,
    val profileUrl: String?
)
