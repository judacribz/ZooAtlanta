package ca.judacribz.zooatlanta.global.view.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import ca.judacribz.zooatlanta.R
import kotlinx.android.synthetic.main.activity_zoo_atlanta_web_view.*


internal class ZooAtlantaWebView : AppCompatActivity() {

    companion object {
        const val EXTRA_URL = "ca.judacribz.week2weekend.global.view.activities.EXTRA_URL"

        fun openActivity(context: Context, url: String) {
            Intent(context, ZooAtlantaWebView::class.java).apply {
                putExtra(EXTRA_URL, url)
                context.startActivity(this)
            }
        }
    }

    private var _menu: Menu? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zoo_atlanta_web_view)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_home)
            setBackgroundDrawable(getDrawable(R.color.colorZooAtlanta))
            title = ""
        }

        webview.apply {
            settings.apply {
                javaScriptEnabled = true
                allowContentAccess = true
                domStorageEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
            }

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest
                ): Boolean {
                    view.loadUrl(request.url.toString())
                    handleMenuItems()
                    return false
                }
            }

            loadUrl(intent.getStringExtra(EXTRA_URL))
        }
    }

    override fun onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack()
            handleMenuItems()
        } else {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.zoo_atlanta_web_view_act_forward -> webview.goForward()
        }
        handleMenuItems()
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_zoo_atlanta_web_view, menu)
        _menu = menu
        return true
    }

    private fun handleMenuItems() {
        _menu?.apply {
            findItem(R.id.zoo_atlanta_web_view_act_forward).isVisible = webview.canGoForward()
        }
    }
}