package ca.judacribz.week2weekend.custom

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

open class BaseViewModel : ViewModel() {

    private val job by lazyOf(Job())

    val uiMainScope = CoroutineScope(job + Dispatchers.Main)
    val bgIOScope = CoroutineScope(job + Dispatchers.IO)
    val bgDefaultScope = CoroutineScope(job + Dispatchers.Default)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}