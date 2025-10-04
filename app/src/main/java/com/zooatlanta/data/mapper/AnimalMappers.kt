package com.zooatlanta.data.mapper

import com.zooatlanta.data.local.AnimalCategoryEntity
import com.zooatlanta.data.local.AnimalEntity
import com.zooatlanta.data.remote.AnimalCategoryDto
import com.zooatlanta.data.remote.AnimalDto
import com.zooatlanta.domain.model.Animal
import com.zooatlanta.domain.model.AnimalCategory
import kotlin.math.absoluteValue

fun AnimalDto.toEntity(): AnimalEntity =
    AnimalEntity(
        id = stableId(),
        name = name,
        scientificName = scientificName,
        imageUrl = imageUrl,
        diet = diet,
        habitatRange = habitatRange,
        conservationStatus = conservationStatus,
        profileUrl = profileUrl,
        categoryName = categoryName,
        categorySlug = categorySlug
    )

fun AnimalEntity.toDomain(): Animal =
    Animal(
        id = id,
        name = name,
        scientificName = scientificName,
        imageUrl = imageUrl,
        diet = diet,
        habitatRange = habitatRange,
        conservationStatus = conservationStatus,
        profileUrl = profileUrl,
        categoryName = categoryName,
        categorySlug = categorySlug
    )

fun AnimalCategoryDto.toEntity(): AnimalCategoryEntity =
    AnimalCategoryEntity(
        name = name,
        slug = slug,
        url = url
    )

fun AnimalCategoryEntity.toDomain(): AnimalCategory =
    AnimalCategory(
        id = id,
        name = name,
        slug = slug,
        url = url
    )

private fun AnimalDto.stableId(): Long {
    val source = buildString {
        append(name.trim())
        scientificName?.let { append('|').append(it.trim()) }
        profileUrl?.let { append('|').append(it.trim()) }
    }
    return source.hashCode().toLong().absoluteValue
}
