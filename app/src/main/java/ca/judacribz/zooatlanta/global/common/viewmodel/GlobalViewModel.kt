package ca.judacribz.zooatlanta.global.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.judacribz.zooatlanta.global.common.constants.ID_HOURS_TODAY
import ca.judacribz.zooatlanta.global.common.constants.ID_TODAY
import ca.judacribz.zooatlanta.global.common.constants.URL_ZOO_ATLANTA
import ca.judacribz.zooatlanta.global.common.model.Schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class GlobalViewModel : ViewModel() {

    private val _hasNetworkLiveData = MutableLiveData<Boolean>()
    val hasNetworkLiveData: LiveData<Boolean>
        get() = _hasNetworkLiveData

    private val _scheduleLiveData = MutableLiveData<Schedule>()
    val scheduleLiveData: LiveData<Schedule>
        get() = _scheduleLiveData

    fun setNetwork() = _hasNetworkLiveData.postValue(true)

    fun retrieveSchedule() = viewModelScope.launch(Dispatchers.IO) {
        val zooDocument = Jsoup.connect(URL_ZOO_ATLANTA).get() ?: return@launch
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