package com.liuhc.library.presenter

import com.liuhc.library.view.BaseView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * ================================================
 * MVP中P层的基类
 * 都在BaseMVPActivity和BaseMVPFragment里通过反射实例化
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
            withContext(blockCoroutineExceptionHandler) {
                block()
                // 不捕获该异常的话,在控制台上也看不到该异常的堆栈跟踪信息的打印.这是因为在被取消的协程中 CancellationException 被认为是协程执行结束的正常原因
                // Cancellation是有特殊语义,捕获了意味着需要手动处理取消.CoroutineExceptionHandler不会捕获该异常,这个对外面没啥影响
                // 协程正常运行时被取消,就会产生该异常,如在协程里直接调用cancel(),或者delay时间未到job就被取消,或者网络没返回的时候scope取消了等等.
                // 因为本项目的scope是lifecycleScope,在这里如果手动捕获到该异常说明页面被销毁了
//                try {
//                    block()
//                } catch (e: CancellationException) {
//                    e.printStackTrace()
//                    throw e
//                }
            }
            if (showLoading) {
                baseView.hideLoading()
            }
        }
//            .invokeOnCompletion {
//            if (showLoading) {
//                baseView.hideLoading()
//            }
//        }
    }
}