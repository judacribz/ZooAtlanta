package ca.judacribz.zooatlanta.homepage.view.activity

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import ca.judacribz.zooatlanta.R
import ca.judacribz.zooatlanta.animals.AnimalsActivity
import ca.judacribz.zooatlanta.categories.CategoriesActivity
import ca.judacribz.zooatlanta.global.base.BaseActivity
import ca.judacribz.zooatlanta.global.common.activity.WebViewActivity
import ca.judacribz.zooatlanta.homepage.viewmodel.HomePageViewModel
import kotlinx.android.synthetic.main.activity_homepage.apHomepageCustom

class HomePageActivity : BaseActivity() {

    private val _homePageViewModel: HomePageViewModel by viewModels()

    override fun getLayoutResource(): Int = R.layout.activity_homepage

    override fun onPostCreateView() {
        apHomepageCustom.bind { url -> WebViewActivity.openActivity(this, url) }
        setUpObservers()
    }

    fun goToCategories(@Suppress("UNUSED_PARAMETER") view: View?) {
        startActivity(Intent(this, CategoriesActivity::class.java))
    }

    fun goToAnimals(@Suppress("UNUSED_PARAMETER") view: View?) {
        startActivity(Intent(this, AnimalsActivity::class.java))
    }

    private fun setUpObservers() {
        globalViewModel.hasNetworkLiveData.observe(this, Observer {
            if (it) _homePageViewModel.pullData()
        })

        _homePageViewModel.post.observe(this, Observer { post ->
            apHomepageCustom.post = post
            apHomepageCustom.startAnimation()
        })
    }
}