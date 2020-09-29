package com.liuhc.library.presenter.view

/**
 * ================================================
 * MVP中视图回调 基类
 * Created by liuhc on 2018/3/9
 * ================================================
 */
interface BaseView {
    fun showLoading()
    fun showLoading(text: String)
    fun hideLoading()
    fun onError(text:String)
}
