package ca.judacribz.zooatlanta.animals

import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ca.judacribz.zooatlanta.R
import ca.judacribz.zooatlanta.categories.CategoriesActivity
import ca.judacribz.zooatlanta.global.base.BaseActivity
import ca.judacribz.zooatlanta.global.common.enums.LoadingStatus
import kotlinx.android.synthetic.main.activity_animals.rvAnimals

class AnimalsActivity : BaseActivity() {

    companion object {
        const val ALL_ANIMALS = "All Animals"
    }

    override fun getLayoutResource() = R.layout.activity_animals

    override fun onPostCreateView() {
        val viewModel by viewModels<AnimalsViewModel>()
        viewModel.animalsData.observe(this, Observer {
            when (it.first) {
                LoadingStatus.IN_PROGRESS -> showLoading()
                LoadingStatus.ON_SUCCESS -> {
                    hideLoading()
                    rvAnimals.apply {
                        layoutManager = LinearLayoutManager(this@AnimalsActivity)
                        adapter = AnimalAdapter(it.second!!)
                    }
                }
                else -> Unit
            }
        })
        viewModel.getAnimals(intent.getStringExtra(CategoriesActivity.EXTRA_CATEGORY_NAME)
            ?: ALL_ANIMALS)
    }
}