package ca.judacribz.zooatlanta.animal_details

import androidx.activity.viewModels
import androidx.lifecycle.Observer
import ca.judacribz.zooatlanta.R
import ca.judacribz.zooatlanta.animals.AnimalAdapter
import ca.judacribz.zooatlanta.global.base.BaseActivity
import ca.judacribz.zooatlanta.global.common.enums.LoadingStatus
import ca.judacribz.zooatlanta.models.Animal
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_animal_details.ivAnimalDetailsMain
import kotlinx.android.synthetic.main.activity_animal_details.tvAnimalDetailsDescription
import kotlinx.android.synthetic.main.activity_animal_details.tvAnimalName
import kotlinx.android.synthetic.main.activity_animal_details.tvDiet
import kotlinx.android.synthetic.main.activity_animal_details.tvHabitat
import kotlinx.android.synthetic.main.activity_animal_details.tvRange
import kotlinx.android.synthetic.main.activity_animal_details.tvScientificName
import kotlinx.android.synthetic.main.activity_animal_details.tvStatus
import kotlinx.android.synthetic.main.activity_animal_details.tvViewingHints

class AnimalDetailsActivity : BaseActivity() {

    override fun getLayoutResource() = R.layout.activity_animal_details

    override fun onPostCreateView() {
        intent.extras?.getParcelable<Animal>(AnimalAdapter.EXTRA_ANIMAL)?.apply {
            Glide
                .with(this@AnimalDetailsActivity)
                .load(imgUrl)
                .into(ivAnimalDetailsMain)

            getOtherDetails(name)

            tvAnimalName!!.text = name
            tvScientificName!!.text = scientificName
            tvDiet!!.text = diet
            tvStatus!!.text = status
            tvRange!!.text = range
        }
    }

    private fun getOtherDetails(name: String?) {
        if (name == null) return

        val viewModel by viewModels<AnimalDetailsViewModel>()
        viewModel.animalsData.observe(this, Observer {
            when (it.first) {
                LoadingStatus.IN_PROGRESS -> showLoading()
                LoadingStatus.ON_SUCCESS -> {
                    hideLoading()
                    it.second!!.let { animal ->
                        tvAnimalDetailsDescription.text = animal.description
                        tvHabitat.text = animal.habitat
                        tvViewingHints.text = animal.viewingHints
                    }
                }
                else -> Unit
            }
        })
        viewModel.getAnimals(name)
    }
}