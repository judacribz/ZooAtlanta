package ca.judacribz.zooatlanta.global.view.dialog

import android.app.Dialog
import android.content.Context
import ca.judacribz.zooatlanta.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.dialog_progress.ivProgressGif
import kotlinx.android.synthetic.main.dialog_progress.tvProgressLoading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProgressDialog(context: Context) : Dialog(context) {

    companion object {
        val NETWORK_START_INDEX = 0
        val DOT_CYCLE_DIVISOR = 3
    }

    private val _networkLoadingText: String by lazy {
        context.getString(R.string.searching_for_network)
    }
    private var _networkLoading = false

    init {
        setCancelable(false)
        setContentView(R.layout.dialog_progress)

        Glide.with(context)
            .load(R.drawable.loading)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .into(ivProgressGif)
    }

    fun showNetworkLoading() {
        _networkLoading = true
        var i = Int.MAX_VALUE
        GlobalScope.launch {
            do {
                withContext(Dispatchers.Main) {
                    tvProgressLoading.text = _networkLoadingText.substring(
                        NETWORK_START_INDEX,
                        _networkLoadingText.length - i--.rem(DOT_CYCLE_DIVISOR)
                    )
                }
                delay(500)
            } while (_networkLoading)
        }
        show()
    }

    override fun dismiss() {
        super.dismiss()
        _networkLoading = false
        tvProgressLoading.text = ""
    }
}