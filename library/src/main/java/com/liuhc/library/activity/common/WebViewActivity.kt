package com.liuhc.library.activity.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.liuhc.library.R
import com.liuhc.library.activity.base.BaseActivity
import kotlinx.android.synthetic.main.common_activity_webview.*

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2020/10/16 on 6:14 PM
 */
class WebViewActivity : BaseActivity() {

    private val url by lazy { intent.getStringExtra("url") }

    override fun init(savedInstanceState: Bundle?) {
        webView.loadUrl(url)
    }

    override fun getContentView() = R.layout.common_activity_webview

    companion object {
        fun show(context: Context, url: String) {
            context.startActivity(Intent(context, WebViewActivity::class.java).apply { putExtra("url", url) })
        }
    }
}