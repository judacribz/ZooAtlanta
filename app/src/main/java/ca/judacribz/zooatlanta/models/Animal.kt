package ca.judacribz.zooatlanta.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Animal(
    var name: String? = null,
    var description: String? = null,
    var imgUrl: String? = null,
    var scientificName: String? = null,
    var diet: String? = null,
    var status: String? = null,
    var viewingHints: String? = null,
    var range: String? = null,
    var habitat: String? = null
) : Parcelable