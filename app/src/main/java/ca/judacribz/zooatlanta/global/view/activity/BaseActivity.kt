package ca.judacribz.zooatlanta.global.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ca.judacribz.zooatlanta.R
import ca.judacribz.zooatlanta.global.util.isNetworkActive
import kotlinx.android.synthetic.main.toolbar.*

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResource())
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if (isNetworkActive(this)) {
            onPostCreateView()
        }
    }

    protected abstract fun getLayoutResource(): Int

    protected abstract fun onPostCreateView()

    protected fun setUpHomeIcon() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_home)
        }
    }
}
