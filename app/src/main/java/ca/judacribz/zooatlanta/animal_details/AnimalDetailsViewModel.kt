package ca.judacribz.zooatlanta.animal_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.judacribz.zooatlanta.global.common.constants.TAG_P
import ca.judacribz.zooatlanta.global.common.constants.URL_ZOO_ATLANTA
import ca.judacribz.zooatlanta.global.common.enums.LoadingStatus
import ca.judacribz.zooatlanta.global.common.util.getFirstElementByClass
import ca.judacribz.zooatlanta.global.common.util.getFirstElementByTagText
import ca.judacribz.zooatlanta.models.Animal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.util.*

class AnimalDetailsViewModel : ViewModel() {
    companion object {
        const val ABSTRACT = "abstract"
        const val MAIN_CONTAINER = "main-left-content"
        const val INDEX_HABITAT = 3
        const val INDEX_HABITAT_TEXT = 1
        const val INDEX_VIEWING_HINTS = 4
        const val URL_ANIMAL = "$URL_ZOO_ATLANTA/animal"
    }

    private val _animalsData = MutableLiveData<Pair<LoadingStatus, Animal?>>()
    val animalsData: LiveData<Pair<LoadingStatus, Animal?>>
        get() = _animalsData

    fun getAnimals(animalName: String) = viewModelScope.launch(Dispatchers.IO) {
        _animalsData.postValue(LoadingStatus.IN_PROGRESS to null)
        val document = Jsoup.connect(
            "$URL_ANIMAL/${animalName.toLowerCase(Locale.US).replace(' ', '-')}"
        ).get()

        withContext(Dispatchers.Default) {
            val otherDetails = document
                .getFirstElementByClass(MAIN_CONTAINER)
                ?.getElementsByTag(TAG_P)

            _animalsData.postValue(LoadingStatus.ON_SUCCESS to Animal(
                description = "\t\t\t\t${document
                    .getFirstElementByClass(ABSTRACT)
                    ?.getFirstElementByTagText(TAG_P)}",
                habitat = otherDetails
                    ?.get(INDEX_HABITAT)
                    ?.text()
                    ?.split(":")
                    ?.get(INDEX_HABITAT_TEXT),
                viewingHints = otherDetails?.get(INDEX_VIEWING_HINTS)?.text()
            ))
        }
    }
}