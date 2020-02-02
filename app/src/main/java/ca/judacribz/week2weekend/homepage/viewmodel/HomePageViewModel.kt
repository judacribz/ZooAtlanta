package ca.judacribz.week2weekend.homepage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ca.judacribz.week2weekend.custom.BaseViewModel
import ca.judacribz.week2weekend.homepage.model.Schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.TextNode
import java.io.IOException

class HomePageViewModel : BaseViewModel() {
    companion object {
        private const val DURATION_IMAGE_CHANGE: Long = 7000
        private const val ITER_INDEX_BY = 1
    }

    private val _imgInd = MutableLiveData(0)
    val imgInd: LiveData<Int>
        get() = _imgInd
    private val _schedule = MutableLiveData<Schedule>()
    val schedule: LiveData<Schedule>
        get() = _schedule

    var changeImgs = true

    fun setupImages(numImgs: Int) = bgDefaultScope.launch {
        changeImgs = true
        while (changeImgs) {
            delay(DURATION_IMAGE_CHANGE)
            _imgInd.postValue(_imgInd.value?.plus(ITER_INDEX_BY)?.rem(numImgs))
        }
    }

    fun retrieveSchedule() = uiMainScope.launch {
        try {
            val document = withContext(Dispatchers.IO) {
                Jsoup.connect("https://zooatlanta.org/").get()
            }
            val scheduleNode = withContext(Dispatchers.Default) {
                document
                    .getElementById("today")
                    .getElementById("hours-today")
                    .textNodes()
                    .subList(0, 2)
            }
            setSchedule(scheduleNode)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setSchedule(scheduleNode: List<TextNode>) {
        _schedule.value = Schedule(
            scheduleNode[0].toString().trim(),
            scheduleNode[1].toString().trim()
        )
    }
}