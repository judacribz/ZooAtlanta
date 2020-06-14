package ca.judacribz.zooatlanta.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.judacribz.zooatlanta.global.common.constants.URL_ANIMALS
import ca.judacribz.zooatlanta.global.common.constants.URL_CATEGORY_ANIMALS
import ca.judacribz.zooatlanta.global.common.enums.LoadingStatus
import ca.judacribz.zooatlanta.global.common.util.getFirstElementByTag
import ca.judacribz.zooatlanta.models.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class CategoriesViewModel : ViewModel() {

    private val _categoryData = MutableLiveData<Pair<LoadingStatus, Category?>>()
    val categoryData: LiveData<Pair<LoadingStatus, Category?>>
        get() = _categoryData

    fun getCategories() = viewModelScope.launch(Dispatchers.IO) {
        _categoryData.postValue(LoadingStatus.IN_PROGRESS to null)

        var document = Jsoup.connect(URL_ANIMALS).get()

        for (el in document.getElementsByClass("popular-category")) {
            val name = el.getFirstElementByTag("label")?.getFirstElementByTag("input")?.`val`()
                ?: continue

            val numSpecies = Jsoup
                .connect("$URL_CATEGORY_ANIMALS$name")
                .get()
                .getElementsByClass("animal card")
                .size
            document = Jsoup.connect("https://www.vocabulary.com/dictionary/$name").get()

            _categoryData.postValue(LoadingStatus.ON_SUCCESS to Category(
                name,
                document.getElementsByClass("short")[0].text(),
                numSpecies
            ))
        }
    }
}