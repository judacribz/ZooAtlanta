package ca.judacribz.zooatlanta.global.view.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import ca.judacribz.zooatlanta.AppSession
import ca.judacribz.zooatlanta.R
import ca.judacribz.zooatlanta.global.viewmodel.BaseAndroidViewModel
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_schedule.*

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var _receiver: NetworkReceiver

    protected val viewModel: BaseAndroidViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResource())
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        AppSession.schedule?.run {
            tvScheduleSchedule.text = admissionTime
            tvScheduleLastAdmin.text = lastAdmission
        }

        _receiver = NetworkReceiver(viewModel)
        @Suppress("DEPRECATION") //TODO fix
        registerReceiver(_receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        setUpObservers()
        onPostCreateView()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(_receiver)
    }

    private fun setUpObservers() {
        viewModel.hasNetworkLiveData.observe(this, Observer {
            if (it && AppSession.schedule == null) viewModel.retrieveSchedule()
        })

        viewModel.scheduleLiveData.observe(this, Observer { schedule ->
            AppSession.schedule = schedule

            tvScheduleSchedule.text = schedule.admissionTime
            tvScheduleLastAdmin.text = schedule.lastAdmission
        })
    }

    protected abstract fun getLayoutResource(): Int

    protected abstract fun onPostCreateView()

    protected fun setUpHomeIcon() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_home)
        }
    }

    internal class NetworkReceiver(private val viewModel: BaseAndroidViewModel) :
        BroadcastReceiver() {
        private var calledOnce = false
        override fun onReceive(context: Context, intent: Intent?) {
            synchronized(this) {
                if (isNetworkActive(context) && calledOnce.not()) {
                    viewModel.setNetwork()
                    calledOnce = true
                }
            }
        }

        /**
         * Utility function to check if user is connected to internet.
         *
         * @param context - used to retrieve Connectivity Manager.
         */
        @Suppress("DEPRECATION")
        private fun isNetworkActive(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            // API 23 or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork ?: return false
                with(connectivityManager.getNetworkCapabilities(network) ?: return false) {
                    return hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        .or(hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                }
            } else {
                // Deprecated after API 23
                val networkInfo = connectivityManager.activeNetworkInfo ?: return false
                return networkInfo.isConnected
            }
        }
    }
}
