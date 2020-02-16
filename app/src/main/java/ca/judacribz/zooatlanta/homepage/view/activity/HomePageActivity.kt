package ca.judacribz.zooatlanta.homepage.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ca.judacribz.zooatlanta.R
import ca.judacribz.zooatlanta.animals.Animals
import ca.judacribz.zooatlanta.categories.Categories
import ca.judacribz.zooatlanta.global.util.isNetworkActive
import ca.judacribz.zooatlanta.global.view.activity.BaseActivity
import ca.judacribz.zooatlanta.global.view.activity.WebViewActivity
import ca.judacribz.zooatlanta.homepage.viewmodel.HomePageViewModel
import kotlinx.android.synthetic.main.activity_homepage.*

class HomePageActivity : BaseActivity() {
    private lateinit var viewModel: HomePageViewModel

    override fun getLayoutResource(): Int = R.layout.activity_homepage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpViewModel()
    }

    override fun onResume() {
        super.onResume()
        if (::viewModel.isInitialized && viewModel.cyclePosts.not() && viewModel.numPosts > 0)
            viewModel.cyclePosts = true
    }

    override fun onPause() {
        super.onPause()
        viewModel.cyclePosts = false
    }

    fun learnMore(@Suppress("UNUSED_PARAMETER") view: View) {
        viewModel.learnMoreUrl?.let { WebViewActivity.openActivity(this, it) }
    }

    fun goToCategories(@Suppress("UNUSED_PARAMETER") view: View?) {
        startActivity(Intent(this, Categories::class.java))
    }

    fun goToAnimals(@Suppress("UNUSED_PARAMETER") view: View?) {
        startActivity(Intent(this, Animals::class.java))
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this).get(HomePageViewModel::class.java)

        if (isNetworkActive(this)) {
            viewModel.init()
        }

        viewModel.mainPosts.observe(this, Observer { mainAnimalList ->
            if (mainAnimalList.isNotEmpty()) {
                viewModel.postIndex.observe(this, Observer { index ->
                    ivAnimalImages.setImageBitmap(mainAnimalList[index].image)
                    tvAnimalHeadline.text = mainAnimalList[index].headline
                    tvAnimalDescription.text = mainAnimalList[index].shortDescription
                })
            }
        })

        viewModel.schedule.observe(this, Observer { schedule ->
            tvSchedule.text = schedule.admissionTime
            tvLastAdmin.text = schedule.lastAdmission
        })
    }
}