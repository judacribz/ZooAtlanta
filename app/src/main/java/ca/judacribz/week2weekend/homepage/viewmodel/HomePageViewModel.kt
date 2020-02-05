package ca.judacribz.week2weekend.homepage.viewmodel

import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ca.judacribz.week2weekend.custom.BaseViewModel
import ca.judacribz.week2weekend.global.constants.*
import ca.judacribz.week2weekend.global.util.extractUrl
import ca.judacribz.week2weekend.homepage.model.AnimalMain
import ca.judacribz.week2weekend.homepage.model.Schedule
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.net.URL


class HomePageViewModel : BaseViewModel() {
    companion object {
        private val TAG = HomePageViewModel::class.java.simpleName
        private const val DURATION_IMAGE_CHANGE: Long = 5000
    }

    private val _mainImages = MutableLiveData(ArrayList<AnimalMain>())
    val mainImages: MutableLiveData<ArrayList<AnimalMain>>
        get() = _mainImages
    private val _imageIndex = MutableLiveData(0)
    val imageIndex: LiveData<Int>
        get() = _imageIndex
    private val _schedule = MutableLiveData<Schedule>()
    val schedule: LiveData<Schedule>
        get() = _schedule

    private var _numImages = 0

    var cycleImages: Boolean = false
        set(value) {
            if (value) cycleImages()

            field = value
        }

    fun retrieveMainImages() = bgIOScope.launch {
        val document = try {
            withContext(Dispatchers.IO) {
                Jsoup.connect(ZOO_ATLANTA_URL).get()
            }
        } catch (e: IOException) {
            e.apply {
                Log.e(TAG, "retrieveMainImages: ", this)
                printStackTrace()
            }
            return@launch
        }

        document.getElementsByClass(CLASS_SLIDE)?.apply {
            forEach {
                val url = extractUrl(it.getElementsByClass(CLASS_HERO_IMAGE)[0].attr(ATTR_STYLE))
                val bitmap = async(Dispatchers.IO) {
                    BitmapFactory.decodeStream(URL(url).openStream())
                }
                val headline = it.getElementsByTag(TAG_H2)[0].text()
                val body = it.getElementsByTag(TAG_P)[0].text()

                _mainImages.apply {
                    value?.add(AnimalMain(bitmap.await(), headline, body))
                    postValue(value)
                }
                _numImages++
                if (_numImages == 1) {
                    cycleImages = true
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

    private fun cycleImages() = bgDefaultScope.launch {
        var i: Int = _imageIndex.value!!
        while (cycleImages) {

            _imageIndex.postValue(i.rem(_numImages))
            delay(DURATION_IMAGE_CHANGE)
            i++
        }
    }
}