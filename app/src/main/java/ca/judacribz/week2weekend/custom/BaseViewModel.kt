package ca.judacribz.week2weekend.custom

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

open class BaseViewModel : ViewModel() {

    private val job = Job()
    val uiMainScope = CoroutineScope(Dispatchers.Main + job)
    val bgIOScope = CoroutineScope(Dispatchers.IO + job)
    val bgDefaultScope = CoroutineScope(Dispatchers.Default + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}