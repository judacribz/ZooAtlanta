package ca.judacribz.zooatlanta.global.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import ca.judacribz.zooatlanta.R
import ca.judacribz.zooatlanta.global.constants.ZOO_ATLANTA_URL
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : BaseActivity() {

    companion object {
        private const val EXTRA_URL = "ca.judacribz.zooatlanta.global.view.activity.EXTRA_URL"
        fun openActivity(context: Context, url: String) {
            Intent(context, WebViewActivity::class.java).apply {
                putExtra(EXTRA_URL, url)
                context.startActivity(this)
            }
        }
    }

    private var _menu: Menu? = null

    override fun getLayoutResource(): Int = R.layout.activity_web_view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpHomeIcon()
        setUpWebView()
    }

    override fun onBackPressed() {
        if (web_view_wv_page_holder.canGoBack()) {
            web_view_wv_page_holder.goBack()
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
            R.id.zoo_atlanta_web_view_act_forward -> web_view_wv_page_holder.goForward()
        }
        handleMenuItems()
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_zoo_atlanta_web_view, menu)
        _menu = menu
        return true
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        web_view_wv_page_holder.settings.apply {
            javaScriptEnabled = true
            allowContentAccess = true
            domStorageEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
        }

        web_view_wv_page_holder.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                val url = request.url.toString()
                if (url.contains(ZOO_ATLANTA_URL)) {
                    view.loadUrl(url)
                    handleMenuItems()
                    return false
                }

                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        web_view_wv_page_holder.loadUrl(intent.getStringExtra(EXTRA_URL))
    }

    private fun handleMenuItems() {
        _menu?.apply {
            findItem(R.id.zoo_atlanta_web_view_act_forward).isVisible =
                web_view_wv_page_holder.canGoForward()
        }
    }
}