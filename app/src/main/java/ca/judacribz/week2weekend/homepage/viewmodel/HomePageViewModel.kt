package ca.judacribz.week2weekend.homepage.viewmodel

import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ca.judacribz.week2weekend.global.constants.*
import ca.judacribz.week2weekend.global.util.extractUrl
import ca.judacribz.week2weekend.global.util.getFirstElementByTag
import ca.judacribz.week2weekend.global.viewmodel.BaseViewModel
import ca.judacribz.week2weekend.homepage.model.AnimalPost
import ca.judacribz.week2weekend.homepage.model.Schedule
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
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

    private var _numPosts = 0

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

    fun retrieveMainImages() = bgIOScope.launch {
        val document = withContext(Dispatchers.IO) {
            Jsoup.connect(ZOO_ATLANTA_URL).get()
        }

        document.getElementsByClass(CLASS_SLIDE)?.apply {
            forEach {
                val url = extractUrl(it.getElementsByClass(CLASS_HERO_IMAGE)[0].attr(ATTR_STYLE))
                val bitmap = async(Dispatchers.IO) {
                    BitmapFactory.decodeStream(URL(url).openStream())
                }
                val headline = it.getFirstElementByTag(TAG_H2)?.text()
                val shortDescription = it.getFirstElementByTag(TAG_P)?.text()
                val learMoreUrl = it.getFirstElementByTag(TAG_A)?.attr(ATTR_HREF)

                _mainPosts.apply {
                    value?.add(AnimalPost(bitmap.await(), headline, shortDescription, learMoreUrl))
                    postValue(value)
                }
                _numPosts++
                if (_numPosts == 1) {
                    cyclePosts = true
                }
            }
        }
    }

    fun retrieveSchedule() = bgIOScope.launch {
        var document: Document? = null

        try {
            document = withContext(Dispatchers.IO) {
                Jsoup.connect(ZOO_ATLANTA_URL).get()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        withContext(Dispatchers.Main) {
            val scheduleNode = document
                ?.getElementById(ID_TODAY)
                ?.getElementById(ID_HOURS_TODAY)
                ?.textNodes()
                ?.subList(0, 2)

            scheduleNode?.let {
                _schedule.value = Schedule(it[0].toString().trim(), it[1].toString().trim())
            }
        }
    }

    private fun cycleAnimalPosts() {
        var i: Int = _postIndex.value!!

        bgDefaultScope.launch {
            while (cyclePosts) {
                _postIndex.postValue(i.rem(_numPosts))
                delay(DURATION_IMAGE_CHANGE)
                i++
            }
        }
    }
}