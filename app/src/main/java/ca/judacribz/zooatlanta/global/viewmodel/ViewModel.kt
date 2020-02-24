package ca.judacribz.zooatlanta.global.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

open class ViewModel : ViewModel() {

    private val job by lazyOf(Job())

    val bgIOScope = CoroutineScope(job + Dispatchers.IO)
    val bgDefaultScope = CoroutineScope(job + Dispatchers.Default)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}