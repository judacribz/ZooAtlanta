package ca.judacribz.zooatlanta.homepage.view.activity

import android.content.Intent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import ca.judacribz.zooatlanta.R
import ca.judacribz.zooatlanta.animals.Animals
import ca.judacribz.zooatlanta.categories.Categories
import ca.judacribz.zooatlanta.global.view.activity.BaseActivity
import ca.judacribz.zooatlanta.global.view.activity.WebViewActivity
import ca.judacribz.zooatlanta.homepage.model.BasePost
import ca.judacribz.zooatlanta.homepage.viewmodel.HomePageViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_homepage.*

class HomePageActivity : BaseActivity() {

    private val _homePageViewModel: HomePageViewModel by viewModels()

    private lateinit var _animFadeOut: Animation
    private lateinit var _animFadeIn: Animation

    private var _mainAnimalPosts: List<BasePost>? = null
    private var _post: BasePost? = null


    override fun getLayoutResource(): Int = R.layout.activity_homepage

    override fun onPostCreateView() {
        _animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        _animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        setUpMainPostAnimation()
        setUpObservers()
    }

    override fun onResume() {
        super.onResume()
        if (_homePageViewModel.cyclePosts.not() && _homePageViewModel.numPosts > 0)
            _homePageViewModel.cyclePosts = true
    }

    override fun onPause() {
        super.onPause()
        _homePageViewModel.cyclePosts = false
    }

    fun learnMore(@Suppress("UNUSED_PARAMETER") view: View) {
        _homePageViewModel.learnMoreUrl?.let { WebViewActivity.openActivity(this, it) }
    }

    fun goToCategories(@Suppress("UNUSED_PARAMETER") view: View?) {
        startActivity(Intent(this, Categories::class.java))
    }

    fun goToAnimals(@Suppress("UNUSED_PARAMETER") view: View?) {
        startActivity(Intent(this, Animals::class.java))
    }

    private fun setUpMainPostAnimation() {
        _animFadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                // NOP
            }

            override fun onAnimationEnd(animation: Animation?) {
                Glide
                    .with(this@HomePageActivity)
                    .load(_post!!.imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(ivHomepageAnimalImage)

                tvHomepageAnimalHeadline.text = _post!!.headline
                tvHomepageAnimalDescription.text = _post!!.shortDescription

                clHomepageAnimalPost.startAnimation(_animFadeIn)
            }

            override fun onAnimationStart(animation: Animation?) {
                // NOP
            }
        })
    }

    private fun setUpObservers() {
        viewModel.hasNetworkLiveData.observe(this, Observer {
            _homePageViewModel.pullData()
        })

        _homePageViewModel.mainPosts.observe(this, Observer {
            if (it.isNotEmpty()) _mainAnimalPosts = it
        })

        _homePageViewModel.postIndex.observe(this, Observer { index ->
            _mainAnimalPosts?.get(index)?.let {
                _post = it
                clHomepageAnimalPost.startAnimation(_animFadeOut)
            }
        })
    }
}