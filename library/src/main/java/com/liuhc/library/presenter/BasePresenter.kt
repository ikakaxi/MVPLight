package com.liuhc.library.presenter

import com.liuhc.library.view.BaseView
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * ================================================
 * MVP中P层 基类
 * Created by liuhc on 2019/11/8
 * ================================================
 */
open class BasePresenter(private val baseView: BaseView, private val scope: CoroutineScope) {

    /**
     * 默认运行在UI线程的协程
     *
     * @param showLoading  是否显示loading
     * @param blockCoroutineContext  block所在的协程作用域
     * @param block 业务代码块
     */
    fun launchUI(
        showLoading: Boolean = true,
        blockCoroutineContext: CoroutineContext = Dispatchers.Main,
        block: suspend CoroutineScope.() -> Unit
    ) {
        // 处理协程异常
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            scope.launch(Dispatchers.Main) { baseView.onError(e.message ?: "未知错误") }
        }
        // UI协程上下文+处理协程异常
        val uiCoroutineExceptionHandler = Dispatchers.Main + coroutineExceptionHandler
        // 使用参数的协程上下文+处理协程异常
        val blockCoroutineExceptionHandler = blockCoroutineContext + coroutineExceptionHandler
        scope.launch(uiCoroutineExceptionHandler) {
            if (showLoading) {
                baseView.showLoading()
            }
            launch(blockCoroutineExceptionHandler) {
                block()
            }
            if (showLoading) {
                baseView.hideLoading()
            }
        }.invokeOnCompletion {
            if (showLoading) {
                baseView.hideLoading()
            }
        }
    }
}