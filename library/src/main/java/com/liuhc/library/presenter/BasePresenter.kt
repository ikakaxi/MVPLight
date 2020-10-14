package com.liuhc.library.presenter

import com.liuhc.library.presenter.view.BaseView
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ================================================
 * MVP中P层 基类
 * Created by liuhc on 2019/11/8
 * ================================================
 */
open class BasePresenter(private val baseView: BaseView, private val scope: CoroutineScope) {

    /**
     * 运行在UI线程的协程
     *
     * block 业务代码块
     * error 单独处理错误（默认为空）
     */
    fun launchUI(
        showLoading: Boolean = true,
        block: suspend CoroutineScope.() -> Unit
    ) = scope.launch(Dispatchers.Main + CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        if (showLoading) {
            baseView.hideLoading()
        }
        baseView.onError(e.message ?: "未知错误")
    }) {
        if (showLoading) {
            baseView.showLoading()
        }
        block()
        if (showLoading) {
            baseView.hideLoading()
        }
    }

}