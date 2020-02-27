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
        private const val DOT_CYCLE_DIVISOR = 4
        private const val LOAD_DELAY = 800L
    }

    private val _networkLoadingText: String by lazy {
        "   ${context.getString(R.string.searching_for_network)}"
    }
    private val _loadingTextLength: Int by lazy {
        _networkLoadingText.length
    }
    private var _networkLoading = false

    init {
        setCancelable(false)
        setContentView(R.layout.dialog_progress)

        Glide.with(context)
            .load(R.drawable.loading)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
            .into(ivProgressGif)
    }

    override fun dismiss() {
        _networkLoading = false
        super.dismiss()
    }

    fun show(loadingText: String) {
        tvProgressLoading.text = loadingText
        super.show()
    }

    fun showNetworkLoading() {
        _networkLoading = true
        var i = Int.MAX_VALUE
        GlobalScope.launch {
            do {
                val offset = i--.rem(DOT_CYCLE_DIVISOR)
                withContext(Dispatchers.Main) {
                    tvProgressLoading.text = _networkLoadingText.substring(
                        offset,
                        _loadingTextLength - offset
                    )
                }
                delay(LOAD_DELAY)
            } while (_networkLoading)
        }
        super.show()
    }
}