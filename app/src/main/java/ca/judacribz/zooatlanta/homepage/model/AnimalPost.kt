package ca.judacribz.zooatlanta.homepage.model

import android.graphics.Bitmap

data class AnimalPost(
    val image: Bitmap,
    val headline: String?,
    val shortDescription: String?,
    val learnMoreUrl: String?
)