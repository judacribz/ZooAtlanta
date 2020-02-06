package ca.judacribz.week2weekend.homepage.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ca.judacribz.week2weekend.R
import ca.judacribz.week2weekend.animals.Animals
import ca.judacribz.week2weekend.categories.Categories
import ca.judacribz.week2weekend.global.view.activities.ZooAtlantaWebView
import ca.judacribz.week2weekend.homepage.viewmodel.HomePageViewModel
import kotlinx.android.synthetic.main.activity_homepage.*

class HomePageActivity : AppCompatActivity() {

    private lateinit var viewModel: HomePageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        supportActionBar?.hide()

        setUpViewModel()
    }

    override fun onResume() {
        super.onResume()
        if (::viewModel.isInitialized && viewModel.cyclePosts.not())
            viewModel.cyclePosts = true
    }

    override fun onPause() {
        super.onPause()
        viewModel.cyclePosts = false
    }

    fun learnMore(@Suppress("UNUSED_PARAMETER") view: View) {
        viewModel.learnMoreUrl?.let { ZooAtlantaWebView.openActivity(this, it) }
    }

    fun goToCategories(@Suppress("UNUSED_PARAMETER") view: View?) {
        startActivity(Intent(this, Categories::class.java))
    }

    fun goToAnimals(@Suppress("UNUSED_PARAMETER") view: View?) {
        startActivity(Intent(this, Animals::class.java))
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this).get(HomePageViewModel::class.java).apply {
            retrieveMainImages()
            retrieveSchedule()
        }

        viewModel.mainPosts.observe(this, Observer { mainAnimalList ->
            if (mainAnimalList.isNullOrEmpty().not()) {
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