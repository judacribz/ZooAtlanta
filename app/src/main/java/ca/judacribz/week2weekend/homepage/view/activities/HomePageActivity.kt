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
import ca.judacribz.week2weekend.homepage.model.remote.asynctask.ScheduleTask
import ca.judacribz.week2weekend.homepage.viewmodel.HomePageViewModel
import kotlinx.android.synthetic.main.activity_homepage.*
import org.jsoup.nodes.TextNode


class HomePageActivity : AppCompatActivity(), ScheduleTask.Listener {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        supportActionBar?.hide()

        val scheduleScrape = ScheduleTask(this)
        scheduleScrape.execute()
        setButtonWidthEqually(resources.displayMetrics.widthPixels / 3)


        setUpViewModel()

    }

    fun goToCategories(view: View?) {
        startActivity(Intent(this, Categories::class.java))
    }

    fun goToAnimals(view: View?) {
        startActivity(Intent(this, Animals::class.java))
    }

    private fun setUpViewModel() {
        ViewModelProvider(this).get(HomePageViewModel::class.java).apply {
            setupImages(IMG_ID_ANIMALS.size)

            imgInd.observe(this@HomePageActivity, Observer { imgInd ->
                ivAnimalImages.setImageResource(IMG_ID_ANIMALS[imgInd])
                tvAnimalHeadline.text = getString(STR_ID_HEAD_ANIMALS[imgInd])
                tvAnimalDescription.text = getString(STR_ID_DESC_ANIMALS[imgInd])
            })
        }
    }

    private fun setButtonWidthEqually(btnWidth: Int) {
        for (btnId in BTN_IDS) {
            findViewById<View>(btnId).layoutParams.width = btnWidth
        }
    }

    override fun onScheduleReceived(schedule: List<TextNode?>?) {
        tvSchedule.text = schedule!![0].toString().trim()
        tvLastAdmin.text = schedule[1].toString()
    }
}