package com.zooatlanta.presentation.animals.model

import com.zooatlanta.domain.model.Animal

fun Animal.toUiModel(): AnimalUiModel = AnimalUiModel(
    id = id,
    name = name,
    scientificName = scientificName,
    imageUrl = imageUrl,
    diet = diet,
    habitatRange = habitatRange,
    conservationStatus = conservationStatus,
    profileUrl = profileUrl
)
