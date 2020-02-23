package ca.judacribz.zooatlanta.homepage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ca.judacribz.zooatlanta.global.constants.*
import ca.judacribz.zooatlanta.global.util.extractUrl
import ca.judacribz.zooatlanta.global.util.getFirstClassByTag
import ca.judacribz.zooatlanta.global.util.getFirstElementByTag
import ca.judacribz.zooatlanta.global.viewmodel.BaseViewModel
import ca.judacribz.zooatlanta.homepage.model.BasePost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class HomePageViewModel : BaseViewModel() {
    companion object {
        private const val DURATION_IMAGE_CHANGE: Long = 2000
    }

    private val _mainPosts = MutableLiveData(ArrayList<BasePost>())
    val mainPosts: MutableLiveData<ArrayList<BasePost>>
        get() = _mainPosts
    private val _postIndex = MutableLiveData(0)
    val postIndex: LiveData<Int>
        get() = _postIndex

    var numPosts = 0

    var cyclePosts: Boolean = false
        set(value) {
            if (value) cycleAnimalPosts()

            field = value
        }
    val learnMoreUrl: String?
        get() {
            _mainPosts.value?.let {
                if (it.isNotEmpty())
                    return it[_postIndex.value!!].learnMoreUrl
            }

            return null
        }

    fun pullData() = bgIOScope.launch(Dispatchers.IO) {
        val zooDocument = Jsoup.connect(ZOO_ATLANTA_URL).get() ?: return@launch

        launch(Dispatchers.IO) {
            retrieveMainImages(zooDocument)
        }
    }

    private suspend fun retrieveMainImages(zooDocument: Document) = withContext(Dispatchers.IO) {
        zooDocument.getElementsByClass(CLASS_SLIDE)?.forEach {
            launch {
                val imageUrl = extractUrl(it.getFirstClassByTag(CLASS_HERO_IMAGE)?.attr(ATTR_STYLE))
                val headline = it.getFirstElementByTag(TAG_H2)?.text()
                val shortDescription = it.getFirstElementByTag(TAG_P)?.text()
                val learMoreUrl = it.getFirstElementByTag(TAG_A)?.attr(ATTR_HREF)

                _mainPosts.apply {
                    synchronized(this) {
                        value!!.add(BasePost(imageUrl, headline, shortDescription, learMoreUrl))
                    }
                    postValue(value!!)
                    numPosts = value!!.size

                    cyclePosts = true
                }
            }
        }
    }

    private fun cycleAnimalPosts() {
        var i: Int = _postIndex.value!!
        bgDefaultScope.launch(Dispatchers.IO) {
            while (cyclePosts) {
                _postIndex.postValue(i.rem(numPosts))
                delay(DURATION_IMAGE_CHANGE)
                synchronized(this) {
                    i++
                }
            }
        }
    }
}