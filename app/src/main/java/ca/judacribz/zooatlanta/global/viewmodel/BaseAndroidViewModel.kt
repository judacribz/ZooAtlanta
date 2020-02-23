package ca.judacribz.zooatlanta.global.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ca.judacribz.zooatlanta.global.constants.ID_HOURS_TODAY
import ca.judacribz.zooatlanta.global.constants.ID_TODAY
import ca.judacribz.zooatlanta.global.constants.ZOO_ATLANTA_URL
import ca.judacribz.zooatlanta.global.model.Schedule
import kotlinx.coroutines.*
import org.jsoup.Jsoup

class BaseAndroidViewModel : ViewModel() {

    private val _scheduleLiveData = MutableLiveData<Schedule>()
    val scheduleLiveData: LiveData<Schedule>
        get() = _scheduleLiveData

    private val _hasNetworkLiveData = MutableLiveData<Boolean>()
    val hasNetworkLiveData: LiveData<Boolean>
        get() = _hasNetworkLiveData

    private val job by lazyOf(Job())

    private val bgIOScope = CoroutineScope(job + Dispatchers.IO)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun setNetwork() = _hasNetworkLiveData.postValue(true)

    fun retrieveSchedule() = bgIOScope.launch(Dispatchers.IO) {
        val zooDocument = Jsoup.connect(ZOO_ATLANTA_URL).get() ?: return@launch

        withContext(Dispatchers.Default) {
            val scheduleNode = zooDocument
                .getElementById(ID_TODAY)
                ?.getElementById(ID_HOURS_TODAY)
                ?.textNodes()
                ?.subList(0, 2) ?: return@withContext

            _scheduleLiveData.postValue(
                Schedule(
                    scheduleNode[0].toString().trim(),
                    scheduleNode[1].toString().trim()
                )
            )
        }
    }
}