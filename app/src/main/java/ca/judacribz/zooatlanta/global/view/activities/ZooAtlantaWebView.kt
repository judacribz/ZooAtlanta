package ca.judacribz.zooatlanta.global.view.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ca.judacribz.zooatlanta.R
import kotlinx.android.synthetic.main.activity_zoo_atlanta_web_view.*

class ZooAtlantaWebView : AppCompatActivity() {

    companion object {
        const val EXTRA_URL = "ca.judacribz.week2weekend.global.view.activities.EXTRA_URL"

        fun openActivity(context: Context, url: String) {
            Intent(context, ZooAtlantaWebView::class.java).apply {
                putExtra(EXTRA_URL, url)
                context.startActivity(this)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zoo_atlanta_web_view)
        webview.settings.apply {
            javaScriptEnabled = true
            allowContentAccess = true
            domStorageEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
        }
        webview.loadUrl(intent.getStringExtra(EXTRA_URL))
    }
}