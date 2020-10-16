package com.liuhc.library

import android.app.Application
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
    }
}