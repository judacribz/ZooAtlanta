package ca.judacribz.week2weekend.homepage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ca.judacribz.week2weekend.homepage.model.Schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.TextNode
import java.io.IOException

class HomePageViewModel : ViewModel() {

    companion object {
        private const val DURATION_IMAGE_CHANGE: Long = 9000
    }

    private val _imgInd = MutableLiveData(0)
    val imgInd: LiveData<Int>
        get() = _imgInd
    private val _schedule = MutableLiveData<Schedule>()
    val schedule: LiveData<Schedule>
        get() = _schedule

    fun setupImages(numImgs: Int) = GlobalScope.launch(Dispatchers.IO) {
        var index = 1
        while (true) {
            delay(DURATION_IMAGE_CHANGE)
            _imgInd.postValue(index)
            index = (index + 1) % numImgs
        }
    }

    fun retrieveSchedule() = GlobalScope.launch(Dispatchers.IO) {
        try {
            val document = Jsoup.connect("https://zooatlanta.org/").get()
            val scheduleNode = document
                .getElementById("today")
                .getElementById("hours-today")
                .textNodes()
                .subList(0, 2)
            setSchedule(scheduleNode)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setSchedule(scheduleNode: List<TextNode>) {
        _schedule.postValue(
            Schedule(
                scheduleNode[0].toString().trim(),
                scheduleNode[1].toString().trim()
            )
        )
    }
}