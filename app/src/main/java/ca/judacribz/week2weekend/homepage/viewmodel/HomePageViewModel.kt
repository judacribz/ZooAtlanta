package ca.judacribz.week2weekend.homepage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomePageViewModel : ViewModel() {

    companion object {
        private const val DURATION_IMAGE_CHANGE: Long = 9000
    }

    private val _imgInd = MutableLiveData(0)

    val imgInd: LiveData<Int>
        get() = _imgInd


    fun setupImages(numImgs: Int) = GlobalScope.launch(Dispatchers.IO) {
        var index = 1
        while (true) {
            delay(DURATION_IMAGE_CHANGE)
            _imgInd.postValue(index)
            index = (index + 1) % numImgs
        }
    }

}