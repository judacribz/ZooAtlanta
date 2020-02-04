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
import ca.judacribz.week2weekend.homepage.viewmodel.HomePageViewModel
import kotlinx.android.synthetic.main.activity_homepage.*

class HomePageActivity : AppCompatActivity() {

    companion object {
        private val IMG_ID_ANIMALS = intArrayOf(
            R.drawable.elephant,
            R.drawable.gorilla,
            R.drawable.panda,
            R.drawable.zebra
        )
        private val STR_ID_HEAD_ANIMALS = intArrayOf(
            R.string.elephant,
            R.string.gorilla,
            R.string.panda,
            R.string.zebra
        )
        private val STR_ID_DESC_ANIMALS = intArrayOf(
            R.string.elephant_desc,
            R.string.gorilla_desc,
            R.string.panda_desc,
            R.string.zebra_desc
        )
        private val BTN_IDS = intArrayOf(
            R.id.btnCategories,
            R.id.btnTickets,
            R.id.btnAnimals
        )
    }

    private lateinit var viewModel: HomePageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        supportActionBar?.hide()

        setUpViewModel()
    }

    override fun onResume() {
        super.onResume()
        if (::viewModel.isInitialized && viewModel.cycleImages.first.not())
            viewModel.cycleImages = true to IMG_ID_ANIMALS.size
    }

    override fun onPause() {
        super.onPause()
        viewModel.cycleImages = false to null
    }

    fun goToCategories(@Suppress("UNUSED_PARAMETER") view: View?) {
        startActivity(Intent(this, Categories::class.java))
    }

    fun goToAnimals(@Suppress("UNUSED_PARAMETER") view: View?) {
        startActivity(Intent(this, Animals::class.java))
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this).get(HomePageViewModel::class.java).apply {
            cycleImages = true to IMG_ID_ANIMALS.size
            retrieveSchedule()
        }

        viewModel.imageIndex.observe(this, Observer { imgInd ->
            ivAnimalImages.setImageResource(IMG_ID_ANIMALS[imgInd])
            tvAnimalHeadline.text = getString(STR_ID_HEAD_ANIMALS[imgInd])
            tvAnimalDescription.text = getString(STR_ID_DESC_ANIMALS[imgInd])
        })

        viewModel.schedule.observe(this, Observer { schedule ->
            tvSchedule.text = schedule.admissionTime
            tvLastAdmin.text = schedule.lastAdmission
        })
    }
}