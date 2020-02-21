package ca.judacribz.zooatlanta.homepage.view.activity

import android.content.Intent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

    private lateinit var _viewModel: HomePageViewModel
    private lateinit var _animFadeOut: Animation
    private lateinit var _animFadeIn: Animation

    private var _mainAnimalPosts: List<BasePost>? = null
    private var _post: BasePost? = null


    override fun getLayoutResource(): Int = R.layout.activity_homepage

    override fun onPostCreateView() {
        _animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        _animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        setUpMainPostAnimation()
        setUpViewModel()
    }

    override fun onResume() {
        super.onResume()
        if (::_viewModel.isInitialized && _viewModel.cyclePosts.not() && _viewModel.numPosts > 0)
            _viewModel.cyclePosts = true
    }

    override fun onPause() {
        super.onPause()
        _viewModel.cyclePosts = false
    }

    fun learnMore(@Suppress("UNUSED_PARAMETER") view: View) {
        _viewModel.learnMoreUrl?.let { WebViewActivity.openActivity(this, it) }
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
                    .into(ivAnimalImage)

                tvAnimalHeadline.text = _post!!.headline
                tvAnimalDescription.text = _post!!.shortDescription

                homepage_clAnimalPost.startAnimation(_animFadeIn)
            }

            override fun onAnimationStart(animation: Animation?) {
                // NOP
            }
        })

    }

    private fun setUpViewModel() {
        _viewModel = ViewModelProvider(this).get(HomePageViewModel::class.java)
        _viewModel.mainPosts.observe(this, Observer {
            if (it.isNotEmpty()) _mainAnimalPosts = it
        })

        _viewModel.postIndex.observe(this, Observer { index ->
            _mainAnimalPosts?.get(index)?.let {
                _post = it
                homepage_clAnimalPost.startAnimation(_animFadeOut)
            }
        })

        _viewModel.schedule.observe(this, Observer { schedule ->
            tvSchedule.text = schedule.admissionTime
            tvLastAdmin.text = schedule.lastAdmission
        })
    }
}