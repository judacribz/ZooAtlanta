package ca.judacribz.week2weekend.homepage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ca.judacribz.week2weekend.custom.BaseViewModel
import ca.judacribz.week2weekend.global.constants.SCHEDULE_ID_HOURS_TODAY
import ca.judacribz.week2weekend.global.constants.SCHEDULE_ID_TODAY
import ca.judacribz.week2weekend.global.constants.ZOO_ATLANTA_URL
import ca.judacribz.week2weekend.homepage.model.Schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class HomePageViewModel : BaseViewModel() {
    companion object {
        private const val DURATION_IMAGE_CHANGE: Long = 5000
    }

    private val _imageIndex = MutableLiveData(0)
    val imageIndex: LiveData<Int>
        get() = _imageIndex
    private val _schedule = MutableLiveData<Schedule>()
    val schedule: LiveData<Schedule>
        get() = _schedule

    var cycleImages: Pair<Boolean, Int?> = false to null
        set(pair) {
            if (pair.first) cycleImages(pair.second!!)

            field = pair
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
                ?.getElementById(SCHEDULE_ID_TODAY)
                ?.getElementById(SCHEDULE_ID_HOURS_TODAY)
                ?.textNodes()
                ?.subList(0, 2)

            scheduleNode?.let {
                _schedule.value = Schedule(it[0].toString().trim(), it[1].toString().trim())
            }
        }
    }

    private fun cycleImages(numImgs: Int) = bgDefaultScope.launch {
        var i = _imageIndex.value
        while (cycleImages.first) {
            _imageIndex.postValue(i?.rem(numImgs))
            delay(DURATION_IMAGE_CHANGE)
            i = i?.inc()
        }
    }
}