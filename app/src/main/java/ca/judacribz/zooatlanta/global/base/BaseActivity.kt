package ca.judacribz.zooatlanta.global.base

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import ca.judacribz.zooatlanta.AppSession
import ca.judacribz.zooatlanta.R
import ca.judacribz.zooatlanta.global.common.GlobalViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.dialog_progress.ivProgressGif
import kotlinx.android.synthetic.main.dialog_progress.tvProgressLoading
import kotlinx.android.synthetic.main.view_schedule.tvScheduleLastAdmin
import kotlinx.android.synthetic.main.view_schedule.tvScheduleSchedule
import kotlinx.android.synthetic.main.view_toolbar.toolbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseActivity(private val showHomeIcon: Boolean = false) : AppCompatActivity() {

    companion object {
        private const val DOT_CYCLE_DIVISOR = 4
        private const val LOAD_DELAY = 800L

        private val NETWORK_REQUEST by lazy {
            NetworkRequest.Builder().apply {
                addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            }.build()
        }
    }

    private val _connectivityManager: ConnectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    private val _connectivityCallback by lazy { getConnectivityCallback() }
    private val _progressDialog: ProgressDialog by lazy { ProgressDialog(this) }
    private var _unregisteredCallback = false

    protected val globalViewModel: GlobalViewModel by viewModels()

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpUi()
        setUpObservers()
        onPostCreateView()

        _connectivityManager.registerNetworkCallback(NETWORK_REQUEST, _connectivityCallback)
        _progressDialog.showNetworkLoading()
    }

    private fun setUpUi() {
        setContentView(getLayoutResource())
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            if (showHomeIcon) {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_home)
            }
        }
    }

    final override fun onStart() {
        super.onStart()
        AppSession.schedule?.run {
            tvScheduleSchedule.text = admissionTime
            tvScheduleLastAdmin.text = lastAdmission
        }
    }

    final override fun onStop() {
        super.onStop()
        unregisterNetworkCallback()
    }

    protected abstract fun getLayoutResource(): Int

    protected abstract fun onPostCreateView()

    fun showLoading(loadingText: String = "") = _progressDialog.show(loadingText)

    fun hideLoading() = _progressDialog.dismiss()

    private fun setUpObservers() {
        globalViewModel.hasNetworkLiveData.observe(this, Observer {
            if (it && AppSession.schedule == null) globalViewModel.retrieveSchedule()
        })

        globalViewModel.scheduleLiveData.observe(this, Observer { schedule ->
            AppSession.schedule = schedule

            tvScheduleSchedule.text = schedule.admissionTime
            tvScheduleLastAdmin.text = schedule.lastAdmission
        })
    }

    private fun getConnectivityCallback() = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            if (isNetworkActive()) {
                unregisterNetworkCallback()
                globalViewModel.setNetwork()
                hideLoading()
            }
        }
    }

    /**
     * Helper function to check if user is connected to a network.
     */
    @Suppress("DEPRECATION")
    private fun isNetworkActive(): Boolean {
        // API 23 Min
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            with(_connectivityManager) {
                val activeNetwork = getNetworkCapabilities(activeNetwork) ?: return false

                with(activeNetwork) {
                    return hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        .or(hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                }
            }
        } else {
            // Deprecated >= API 23
            val networkInfo = _connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

    private fun unregisterNetworkCallback() {
        synchronized(this) {
            if (_unregisteredCallback.not()) {
                _unregisteredCallback = true
                _connectivityManager.unregisterNetworkCallback(_connectivityCallback)
            }
        }
    }

    inner class ProgressDialog(context: Context) : Dialog(context) {

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
}
