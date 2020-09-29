package com.liuhc.mvplight.activity

import android.os.Bundle
import com.liuhc.library.activity.BaseActivity
import com.liuhc.mvplight.R

class MainActivity : BaseActivity() {

    override fun init(savedInstanceState: Bundle?) {
        showLoading()
    }

    override fun getContentView() = R.layout.activity_main
}