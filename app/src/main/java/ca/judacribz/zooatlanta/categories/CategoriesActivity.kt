package ca.judacribz.zooatlanta.categories

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ca.judacribz.zooatlanta.R
import ca.judacribz.zooatlanta.animals.AnimalsActivity
import ca.judacribz.zooatlanta.global.base.BaseActivity
import ca.judacribz.zooatlanta.global.common.enums.LoadingStatus
import ca.judacribz.zooatlanta.models.Category
import kotlinx.android.synthetic.main.activity_categories.rvCategoriesList

class CategoriesActivity : BaseActivity() {

    companion object {
        const val EXTRA_CATEGORY_NAME =
            "ca.judacribz.week2weekend.categories.EXTRA_CATEGORY_NAME"
    }

    override fun getLayoutResource() = R.layout.activity_categories

    override fun onPostCreateView() {

        val viewModel by viewModels<CategoriesViewModel>()
        val adapter: CategoryAdapter
        rvCategoriesList.layoutManager = LinearLayoutManager(this)
        rvCategoriesList.adapter = CategoryAdapter { onCategoryClick(it) }.also { adapter = it }
        viewModel.categoryData.observe(this, Observer {
            when (it.first) {
                LoadingStatus.IN_PROGRESS -> hideLoading()
                LoadingStatus.ON_SUCCESS -> {
                    adapter.addCategory(it.second!!)
                }
                else -> Unit
            }
        })

        viewModel.getCategories()
    }

    private fun onCategoryClick(category: Category) {
        Intent(this, AnimalsActivity::class.java).apply {
            putExtra(EXTRA_CATEGORY_NAME, category.name)
            startActivity(this)
        }
    }
}