package ca.judacribz.zooatlanta.global.view.activity

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

    val viewModel: BaseAndroidViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResource())
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        AppSession.schedule?.run {
            tvScheduleSchedule.text = admissionTime
            tvScheduleLastAdmin.text = lastAdmission
        }

        setUpObservers()
    }

    private fun setUpObservers() {
        viewModel.checkNetwork(AppSession, this)

        viewModel.hasNetworkLiveData.observe(this, Observer {
            onPostCreateView()
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
}
