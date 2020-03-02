package ca.judacribz.zooatlanta.homepage.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import ca.judacribz.zooatlanta.R
import ca.judacribz.zooatlanta.homepage.model.BasePost
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.view_animal_post.view.btnAnimalPostLearnMore
import kotlinx.android.synthetic.main.view_animal_post.view.ivAnimalPostImage
import kotlinx.android.synthetic.main.view_animal_post.view.tvAnimalPostDescription
import kotlinx.android.synthetic.main.view_animal_post.view.tvAnimalPostHeadline

class AnimalPosts @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var _animFadeOut: Animation
    private var _animFadeIn: Animation

    var post: BasePost? = null

    init {
        inflate(context, R.layout.view_animal_post, this)

        _animFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out)
        _animFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
    }

    fun bind(onLearnMoreClick: (url: String) -> Unit) {
        btnAnimalPostLearnMore.setOnClickListener { _ ->
            post?.let { it -> onLearnMoreClick(it.learnMoreUrl) }
        }

        _animFadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                // NOP
            }

            override fun onAnimationEnd(animation: Animation?) {
                Glide
                    .with(context)
                    .load(post!!.imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(ivAnimalPostImage)

                tvAnimalPostHeadline.text = post!!.headline
                tvAnimalPostDescription.text = post!!.shortDescription
                btnAnimalPostLearnMore.visibility = VISIBLE

                startAnimation(_animFadeIn)
            }

            override fun onAnimationStart(animation: Animation?) {
                // NOP
            }
        })
    }

    fun startAnimation() {
        startAnimation(_animFadeOut)
    }
}