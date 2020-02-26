package ca.judacribz.zooatlanta.global.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ca.judacribz.zooatlanta.global.constants.ID_HOURS_TODAY
import ca.judacribz.zooatlanta.global.constants.ID_TODAY
import ca.judacribz.zooatlanta.global.constants.ZOO_ATLANTA_URL
import ca.judacribz.zooatlanta.global.model.Schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class BaseViewModel : ViewModel() {

    private val _hasNetworkLiveData = MutableLiveData<Boolean>()
    val hasNetworkLiveData: LiveData<Boolean>
        get() = _hasNetworkLiveData

    private val _scheduleLiveData = MutableLiveData<Schedule>()
    val scheduleLiveData: LiveData<Schedule>
        get() = _scheduleLiveData

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