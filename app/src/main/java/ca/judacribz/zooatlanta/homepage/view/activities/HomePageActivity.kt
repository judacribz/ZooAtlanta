package ca.judacribz.zooatlanta.homepage.view.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ca.judacribz.zooatlanta.R
import ca.judacribz.zooatlanta.animals.Animals
import ca.judacribz.zooatlanta.categories.Categories
import ca.judacribz.zooatlanta.global.view.activities.ZooAtlantaWebView
import ca.judacribz.zooatlanta.homepage.model.BasePost
import ca.judacribz.zooatlanta.homepage.viewmodel.HomePageViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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
        if (::viewModel.isInitialized && viewModel.cyclePosts.not() && viewModel.numPosts > 0)
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
        val animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        var mainAnimalPosts: List<BasePost>? = null
        var post: BasePost? = null

        animFadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                // NOP
            }

            override fun onAnimationEnd(animation: Animation?) {
                Glide
                    .with(this@HomePageActivity)
                    .load(post!!.imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(ivAnimalImage)

                tvAnimalHeadline.text = post!!.headline
                tvAnimalDescription.text = post!!.shortDescription

                homepage_clAnimalPost.startAnimation(animFadeIn)
            }

            override fun onAnimationStart(animation: Animation?) {
                // NOP
            }
        })

        viewModel = ViewModelProvider(this).get(HomePageViewModel::class.java)

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        if (activeNetwork?.isConnectedOrConnecting == true) {
            viewModel.init()
        }

        viewModel.mainPosts.observe(this, Observer {
            if (it.isNotEmpty()) mainAnimalPosts = it
        })

        viewModel.postIndex.observe(this, Observer { index ->
            mainAnimalPosts?.get(index)?.let {
                post = it
                homepage_clAnimalPost.startAnimation(animFadeOut)
            }
        })

        viewModel.schedule.observe(this, Observer { schedule ->
            tvSchedule.text = schedule.admissionTime
            tvLastAdmin.text = schedule.lastAdmission
        })
    }
}