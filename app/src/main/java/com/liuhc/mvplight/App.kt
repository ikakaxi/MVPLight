package com.liuhc.mvplight

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.liuhc.library.LibraryInitHelper
import com.liuhc.library.net.HttpEventListener
import com.liuhc.library.net.LogInterceptor
import com.liuhc.library.net.NullTypeAdapterFactory
import com.liuhc.library.net.RetrofitFactory
import com.liuhc.library.utils.SDCardUtil
import com.liuhc.library.utils.ToastUtil
import okhttp3.Cache
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2020/9/29 on 3:35 PM
 */
class App : Application() {

    companion object {
        lateinit var retrofit: Retrofit
            private set
    }

    override fun onCreate() {
        super.onCreate()
        retrofit = createRetrofit()
        LibraryInitHelper.init(this)
        ToastUtil.init(this, R.layout.layout_toast)
    }

    private fun createRetrofit(): Retrofit {
        //ctx.getApplicationInfo().flags and ApplicationInfo.FLAG_DEBUGGABLE 在debug状态下值为2,所以非0就是debug状态
        // 指定缓存路径,缓存大小100Mb
        val cacheFile = File(SDCardUtil.getDiskCacheRootDir(this), "HttpCache")
        val cache = Cache(cacheFile, 1024 * 1024 * 100)
        val gson: Gson = GsonBuilder()
            .serializeNulls()
            .registerTypeAdapterFactory(NullTypeAdapterFactory())
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create()
        retrofit = RetrofitFactory()
            .baseUrl("https://www.wanandroid.com")
            .interceptors(LogInterceptor)
            .eventListenerFactory(HttpEventListener.FACTORY)
            .timeOutSecond(10)
            .convertersFactories(GsonConverterFactory.create(gson))
            .callAdapterFactories(RxJava2CallAdapterFactory.create())
            .cache(cache)
            .create()
        return retrofit
    }

}