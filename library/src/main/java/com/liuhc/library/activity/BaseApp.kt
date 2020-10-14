package com.liuhc.library.activity

import android.app.Application
import android.content.Context

/// author:liuhaichao
/// description:
/// create date：2020-10-13 on 4:37 PM
open class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        lateinit var context: Context
    }
}