package ca.judacribz.zooatlanta.animals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.judacribz.zooatlanta.global.common.constants.URL_ANIMALS
import ca.judacribz.zooatlanta.global.common.constants.URL_CATEGORY_ANIMALS
import ca.judacribz.zooatlanta.global.common.enums.LoadingStatus
import ca.judacribz.zooatlanta.global.common.util.getFirstElementByClass
import ca.judacribz.zooatlanta.global.common.util.getFirstElementByTagText
import ca.judacribz.zooatlanta.models.Animal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class AnimalsViewModel : ViewModel() {

    companion object {
        private const val ALL_ANIMALS = "All Animals"
        private const val DIET = "Diet "
        private const val STATUS = " Status In The Wild "
        private const val RANGE = " Range "
        private const val READ_MORE = " Read More"
    }

    private val _animalsData = MutableLiveData<Pair<LoadingStatus, ArrayList<Animal>?>>()
    val animalsData: LiveData<Pair<LoadingStatus, ArrayList<Animal>?>>
        get() = _animalsData

    fun getAnimals(categoryName: String) = viewModelScope.launch(Dispatchers.IO) {
        _animalsData.postValue(LoadingStatus.IN_PROGRESS to null)

        val document = Jsoup.connect(when (categoryName) {
            ALL_ANIMALS -> URL_ANIMALS
            else -> "$URL_CATEGORY_ANIMALS$categoryName"
        }).get()

        withContext(Dispatchers.Default) {
            val animals = ArrayList<Animal>()
            for (aniEl in document.getElementsByClass("animal card")) {
                aniEl.getFirstElementByClass("flipper", "back", "container")?.text()?.let {
                    var beg = it.indexOf(STATUS) + STATUS.length
                    var end = it.indexOf(RANGE)
                    val status = if (beg >= end) "" else it.substring(beg, end)

                    beg = it.indexOf(RANGE) + RANGE.length
                    end = it.indexOf(READ_MORE)
                    val range = if (beg >= end) "" else it.substring(beg, end)

                    val styleStr = aniEl.getFirstElementByClass("featured-image")?.attr("style")
                    animals.add(Animal(
                        name = aniEl.getElementsByTag("h3")[0].text(),
                        imgUrl = styleStr?.substring(
                            styleStr.indexOf('(') + 1,
                            styleStr.indexOf(')')
                        ),
                        scientificName = aniEl.getFirstElementByTagText("em"),
                        diet = it.substring(it.indexOf(DIET) + DIET.length, it.indexOf(STATUS)),
                        status = status,
                        range = range
                    ))
                }
            }

            _animalsData.postValue(LoadingStatus.ON_SUCCESS to animals)
        }
    }
}