package ca.judacribz.zooatlanta.global.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

open class ViewModel : ViewModel() {

    private val _job by lazyOf(Job())

    val bgIOScope = CoroutineScope(_job + Dispatchers.IO)
    val bgDefaultScope = CoroutineScope(_job + Dispatchers.Default)

    final override fun onCleared() {
        super.onCleared()
        _job.cancel()
    }
}