package ca.judacribz.zooatlanta.homepage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.judacribz.zooatlanta.global.common.constants.ATTR_STYLE
import ca.judacribz.zooatlanta.global.common.constants.TAG_H2
import ca.judacribz.zooatlanta.global.common.constants.TAG_P
import ca.judacribz.zooatlanta.global.common.constants.URL_ZOO_ATLANTA
import ca.judacribz.zooatlanta.global.common.util.extractImageUrl
import ca.judacribz.zooatlanta.global.common.util.getFirstATagUrl
import ca.judacribz.zooatlanta.global.common.util.getFirstElementByClass
import ca.judacribz.zooatlanta.global.common.util.getFirstElementByTagText
import ca.judacribz.zooatlanta.homepage.model.BasePost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class HomePageViewModel : ViewModel() {
    companion object {
        private const val DURATION_IMAGE_CHANGE: Long = 2000

        // To retrieve homepage images and text
        private const val CLASS_SLIDE = "slide"
        private const val CLASS_HERO_IMAGE = "hero-image"
    }

    private val _post = MutableLiveData<BasePost>()
    val post: LiveData<BasePost>
        get() = _post

    private val _mainPosts = ArrayList<BasePost>()
    private var _cyclePosts: Boolean = false
        set(value) {
            field = value
            if (value) cycleAnimalPosts()
        }

    fun pullData() = viewModelScope.launch(Dispatchers.IO) {
        val zooDocument = Jsoup.connect(URL_ZOO_ATLANTA).get() ?: return@launch
        retrieveMainImages(zooDocument)
    }

    private fun retrieveMainImages(zooDocument: Document) {
        zooDocument.getElementsByClass(CLASS_SLIDE)?.forEach {
            viewModelScope.launch(Dispatchers.Default) {
                val imageUrl =
                    extractImageUrl(it.getFirstElementByClass(CLASS_HERO_IMAGE)?.attr(ATTR_STYLE))
                        ?: return@launch
                val headline = it.getFirstElementByTagText(TAG_H2) ?: return@launch
                val shortDescription = it.getFirstElementByTagText(TAG_P) ?: return@launch
                val learMoreUrl = it.getFirstATagUrl() ?: return@launch

                _mainPosts.add(BasePost(imageUrl, headline, shortDescription, learMoreUrl))

                if (_cyclePosts.not()) _cyclePosts = true
            }
        }
    }

    private fun cycleAnimalPosts() {
        var postIndex = 0
        viewModelScope.launch(Dispatchers.IO) {
            while (_cyclePosts) {
                _post.postValue(_mainPosts[postIndex.rem(_mainPosts.size)])
                synchronized(this) { postIndex++ }
                delay(DURATION_IMAGE_CHANGE)
            }
        }
    }
}
