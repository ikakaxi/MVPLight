package com.liuhc.library

import android.app.Application
import android.content.pm.ApplicationInfo
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk


/**
 * 描述:在App层调用此方法初始化一些配置
 * 作者:liuhaichao
 * 创建日期：2020/10/16 on 6:09 PM
 */
object LibraryInitHelper {
    fun init(context: Application) {
        // 1.设置开启优化方案
        // 在调用TBS初始化、创建X5WebView之前进行如下配置
        val map = HashMap<String, Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)
        //ctx.getApplicationInfo().flags and ApplicationInfo.FLAG_DEBUGGABLE 在debug状态下值为2,所以非0就是debug状态
        val isDebug = context.applicationInfo != null && context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return isDebug
            }
        })
    }
}