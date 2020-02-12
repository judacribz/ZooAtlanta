package ca.judacribz.zooatlanta.homepage.viewmodel

import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ca.judacribz.zooatlanta.global.constants.*
import ca.judacribz.zooatlanta.global.util.extractUrl
import ca.judacribz.zooatlanta.global.util.getFirstElementByTag
import ca.judacribz.zooatlanta.global.viewmodel.BaseViewModel
import ca.judacribz.zooatlanta.homepage.model.AnimalPost
import ca.judacribz.zooatlanta.homepage.model.Schedule
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URL

class HomePageViewModel : BaseViewModel() {
    companion object {
        private const val DURATION_IMAGE_CHANGE: Long = 5000
    }

    private val _mainPosts = MutableLiveData(ArrayList<AnimalPost>())
    val mainPosts: MutableLiveData<ArrayList<AnimalPost>>
        get() = _mainPosts
    private val _postIndex = MutableLiveData(0)
    val postIndex: LiveData<Int>
        get() = _postIndex
    private val _schedule = MutableLiveData<Schedule>()
    val schedule: LiveData<Schedule>
        get() = _schedule

    var numPosts = 0

    init {
        pullData()
    }

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

    private fun pullData() = uiMainScope.launch(Dispatchers.IO) {
        val zooDocument = Jsoup.connect(ZOO_ATLANTA_URL).get() ?: return@launch

        launch(Dispatchers.IO) {
            retrieveMainImages(zooDocument)
        }

        launch(Dispatchers.IO) {
            retrieveSchedule(zooDocument)
        }
    }

    private suspend fun retrieveMainImages(zooDocument: Document) {
        zooDocument.getElementsByClass(CLASS_SLIDE)?.apply {
            forEach {
                val url = extractUrl(it.getElementsByClass(CLASS_HERO_IMAGE)[0].attr(ATTR_STYLE))
                val bitmap = bgDefaultScope.async(Dispatchers.IO) {
                    BitmapFactory.decodeStream(URL(url).openStream())
                }
                val headline = it.getFirstElementByTag(TAG_H2)?.text()
                val shortDescription = it.getFirstElementByTag(TAG_P)?.text()
                val learMoreUrl = it.getFirstElementByTag(TAG_A)?.attr(ATTR_HREF)

                _mainPosts.apply {
                    value!!.add(AnimalPost(bitmap.await(), headline, shortDescription, learMoreUrl))
                    postValue(value!!)
                    numPosts = value!!.size
                }

                if (numPosts == 1) {
                    cyclePosts = true
                }
            }
        }
    }

    private suspend fun retrieveSchedule(zooDocument: Document) {
        withContext(Dispatchers.Main) {
            val scheduleNode = zooDocument
                .getElementById(ID_TODAY)
                ?.getElementById(ID_HOURS_TODAY)
                ?.textNodes()
                ?.subList(0, 2) ?: return@withContext

            _schedule.value = Schedule(
                scheduleNode[0].toString().trim(),
                scheduleNode[1].toString().trim()
            )
        }
    }

    private fun cycleAnimalPosts() {
        var i: Int = _postIndex.value!!
        bgDefaultScope.launch(Dispatchers.Default) {
            while (cyclePosts) {
                _postIndex.postValue(i.rem(numPosts))
                delay(DURATION_IMAGE_CHANGE)
                i++
            }
        }
    }
}