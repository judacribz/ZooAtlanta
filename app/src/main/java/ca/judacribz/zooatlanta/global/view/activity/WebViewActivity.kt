package ca.judacribz.zooatlanta.global.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import ca.judacribz.zooatlanta.R
import ca.judacribz.zooatlanta.databinding.ActivityWebViewBinding
import ca.judacribz.zooatlanta.global.constants.ZOO_ATLANTA_URL

class WebViewActivity : BaseActivity(true) {

    companion object {
        private const val EXTRA_URL = "ca.judacribz.zooatlanta.global.view.activity.EXTRA_URL"
        fun openActivity(context: Context, url: String) {
            Intent(context, WebViewActivity::class.java).apply {
                putExtra(EXTRA_URL, url)
                context.startActivity(this)
            }
        }
    }

    private lateinit var _binding: ActivityWebViewBinding
    private var _menu: Menu? = null

    override fun getBoundView(): View {
        _binding = ActivityWebViewBinding.inflate(layoutInflater)
        return _binding.root
    }

    override fun onPostCreateView() {
        setUpWebView()
    }

    override fun onBackPressed() {
        if (_binding.wvPageHolder.canGoBack()) {
            _binding.wvPageHolder.goBack()
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
        if (item.itemId == R.id.zoo_atlanta_web_view_act_forward)
            _binding.wvPageHolder.goForward()

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
        with(_binding.wvPageHolder) {
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
                    val url = request.url.toString()
                    if (url.contains(ZOO_ATLANTA_URL)) {
                        view.loadUrl(url)
                        handleMenuItems()
                        return false
                    }

                    return super.shouldOverrideUrlLoading(view, request)
                }
            }

            loadUrl(intent.getStringExtra(EXTRA_URL))
        }
    }

    private fun handleMenuItems() {
        _menu?.findItem(R.id.zoo_atlanta_web_view_act_forward)?.isVisible =
            _binding.wvPageHolder.canGoForward()
    }
}