package ca.judacribz.zooatlanta.global.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ca.judacribz.zooatlanta.R
import kotlinx.android.synthetic.main.toolbar.*

abstract class BaseActivity : AppCompatActivity() {

    protected abstract fun getLayoutResource(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResource())
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    fun setUpHomeIcon() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_home)
        }
    }
}
