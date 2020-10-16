package com.liuhc.library.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.ViewGroup
import com.tencent.smtt.sdk.CookieSyncManager
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

/**
 * 描述:封装腾讯x5浏览器
 * 作者:liuhaichao
 * 创建日期：2020-10-16 on 5:53 PM
 */
@SuppressLint("SetJavaScriptEnabled")
class X5WebView(context: Context, attr: AttributeSet) : WebView(context, attr) {

    private val client: WebViewClient = object : WebViewClient() {
        /**
         * 防止加载网页时调起系统浏览器
         */
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            imgReset(view)
            super.onPageFinished(view, url)
        }

    }

    init {
        this.webViewClient = client
        initWebViewSettings()
        this.view.isClickable = true
    }

    private fun imgReset(view: WebView) {
        view.loadUrl(
            """
            javascript:(
                function(){
                    var imgList = document.getElementsByTagName('img'); 
                    for(var i=0;i<imgList.length;i++){
                        var img = imgList[i];
                        img.style.maxWidth = '100%'; 
                        img.style.height = 'auto';
                    }
                }
            )()
            """.trimMargin()
        )
    }

    private fun initWebViewSettings() {
        val webSetting = this.settings
        @Suppress("DEPRECATION")
        // 支持Javascript交互
        webSetting.javaScriptEnabled = true
        // 支持通过JS打开新窗口
        webSetting.javaScriptCanOpenWindowsAutomatically = true
        // 设置可以访问文件
        webSetting.allowFileAccess = true
        webSetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        // 支持缩放
        webSetting.setSupportZoom(true)
        // 设置内置的缩放控件
        webSetting.builtInZoomControls = true
        // 自适应屏幕
        webSetting.useWideViewPort = true
        // 多窗口
        webSetting.setSupportMultipleWindows(false)
        webSetting.loadWithOverviewMode = true
        // 开启 Application Caches 功能
        webSetting.setAppCacheEnabled(true)
        // 开启 database storage API 功能
        // webSetting.databaseEnabled = true
        // 开启 DOM storage API 功能
        webSetting.domStorageEnabled = true
        webSetting.setGeolocationEnabled(true)
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE)
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY)
        webSetting.pluginState = WebSettings.PluginState.ON_DEMAND
        // 设置渲染的优先级
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH)
        // 缓存模式
        webSetting.cacheMode = WebSettings.LOAD_NO_CACHE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        x5WebViewExtension?.setScrollBarFadingEnabled(false)
        setDownloadListener { url, _, _, _, _ ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse(url)
            context.startActivity(intent)
        }
        isHorizontalScrollBarEnabled = false
        isVerticalScrollBarEnabled = false
        // extension
        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        // settings 的设计
        CookieSyncManager.createInstance(context)
        CookieSyncManager.getInstance().sync()
    }

    override fun destroy() {
        // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，
        // 再destroy()
        val parent = parent
        if (parent != null) {
            (parent as ViewGroup).removeView(this)
        }
        stopLoading()
        // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
        @Suppress("DEPRECATION")
        settings?.javaScriptEnabled = false
        clearHistory()
        clearView()
        removeAllViewsInLayout()
        removeAllViews()
        webViewClient = null
        CookieSyncManager.getInstance().stopSync()
        super.destroy()
    }
}