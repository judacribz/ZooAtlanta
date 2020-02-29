package ca.judacribz.zooatlanta.global.view.activity

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
import ca.judacribz.zooatlanta.global.view.dialog.ProgressDialog
import ca.judacribz.zooatlanta.global.viewmodel.BaseViewModel
import kotlinx.android.synthetic.main.view_schedule.tvScheduleLastAdmin
import kotlinx.android.synthetic.main.view_schedule.tvScheduleSchedule
import kotlinx.android.synthetic.main.view_toolbar.toolbar

abstract class BaseActivity(private val showHomeIcon: Boolean = false) : AppCompatActivity() {

    companion object {
        private val NETWORK_REQUEST: NetworkRequest by lazy {
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

    protected val baseViewModel: BaseViewModel by viewModels()

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
        baseViewModel.hasNetworkLiveData.observe(this, Observer {
            if (it && AppSession.schedule == null) baseViewModel.retrieveSchedule()
        })

        baseViewModel.scheduleLiveData.observe(this, Observer { schedule ->
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
                baseViewModel.setNetwork()
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
}
