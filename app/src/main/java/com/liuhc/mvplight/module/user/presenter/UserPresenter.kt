package com.liuhc.mvplight.module.user.presenter

import com.liuhc.library.presenter.BasePresenter
import com.liuhc.library.view.base.BaseView
import com.liuhc.mvplight.App
import com.liuhc.mvplight.common.ServerApi
import com.liuhc.mvplight.module.user.bean.UserBean
import kotlinx.coroutines.CoroutineScope

/**
 *
 * @Description:
 * @author: bxb
 * @Date: 2020/10/15
 */
class UserPresenter(baseView: BaseView, scope: CoroutineScope) : BasePresenter(baseView, scope) {
    /**
     * 登录
     */
    fun login(username: String, password: String, callback: (UserBean) -> Unit) {
        launchUI {
            val userBean = App.retrofit.create(ServerApi::class.java).login(username, password).check()
            callback(userBean)
        }
    }
    /**
     * 注册
     */
    fun register(username: String, password: String,confirmPassword:String, callback: (UserBean) -> Unit) {
        launchUI {
            val userBean = App.retrofit.create(ServerApi::class.java).register(username, password,confirmPassword).check()
            callback(userBean)
        }
    }
}