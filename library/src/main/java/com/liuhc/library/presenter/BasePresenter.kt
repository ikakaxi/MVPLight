package com.liuhc.library.presenter

import com.liuhc.library.view.BaseView
import kotlinx.coroutines.*
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
        // 注意:该方式无法捕获JobCancellationException异常
        // JobCancellationException是internal修饰的类
        // try catch的话可以用CancellationException,它是JobCancellationException的父类,用public修饰
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
                try {
                    block()
                } catch (e: CancellationException) {
                    e.printStackTrace()
                    // 协程正常运行时被取消,就会产生该异常,如在协程里直接调用cancel(),或者delay时间未到job就被取消,或者网络没返回的时候scope取消了.
                    // 因为CancellationException不会继续抛到CoroutineExceptionHandler,所以手动抛出以处理异常.
                    // 不抛出该异常也可以,因为scope使用的是lifecycleScope,如果出现CancellationException则说明页面已经被销毁.
                    // 为了逻辑严谨,可以继续抛出此异常以使CoroutineExceptionHandler能捕获自己抛出的异常,
                    // 但是因为本项目的scope是lifecycleScope,在这里如果捕获到该异常说明页面被销毁了,所以无视该异常.
                    // throw Exception(e.message)
                }
            }
        }.invokeOnCompletion {
            if (showLoading) {
                baseView.hideLoading()
            }
        }
    }
}