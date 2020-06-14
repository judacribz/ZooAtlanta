package ca.judacribz.zooatlanta.global.common.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import ca.judacribz.zooatlanta.R
import ca.judacribz.zooatlanta.global.base.BaseActivity
import ca.judacribz.zooatlanta.global.common.constants.URL_ZOO_ATLANTA
import kotlinx.android.synthetic.main.activity_web_view.wvWebViewContent

class WebViewActivity : BaseActivity(true) {

    companion object {
        private const val EXTRA_URL = "ca.judacribz.zooatlanta.global.common.activity.EXTRA_URL"
        fun openActivity(context: Context, url: String) {
            Intent(context, WebViewActivity::class.java).apply {
                putExtra(EXTRA_URL, url)
                context.startActivity(this)
            }
        }
    }

    private var _menu: Menu? = null

    override fun getLayoutResource(): Int = R.layout.activity_web_view

    override fun onPostCreateView() {
        setUpWebView()
    }

    override fun onBackPressed() {
        if (wvWebViewContent.canGoBack()) {
            wvWebViewContent.goBack()
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
            R.id.zoo_atlanta_web_view_act_forward -> wvWebViewContent.goForward()
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
        wvWebViewContent.settings.apply {
            javaScriptEnabled = true
            allowContentAccess = true
            domStorageEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
        }

        wvWebViewContent.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                val url = request.url.toString()
                if (url.contains(URL_ZOO_ATLANTA)) {
                    view.loadUrl(url)
                    handleMenuItems()
                    return false
                }

                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        wvWebViewContent.loadUrl(intent.getStringExtra(EXTRA_URL)!!)
    }

    private fun handleMenuItems() {
        _menu?.apply {
            findItem(R.id.zoo_atlanta_web_view_act_forward).isVisible =
                wvWebViewContent.canGoForward()
        }
    }
}