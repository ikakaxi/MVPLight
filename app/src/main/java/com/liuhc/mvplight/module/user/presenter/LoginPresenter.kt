package com.liuhc.mvplight.module.user.presenter

import com.liuhc.library.presenter.BasePresenter
import com.liuhc.library.presenter.view.BaseView
import com.liuhc.mvplight.App
import com.liuhc.mvplight.common.ServerApi
import com.liuhc.mvplight.module.user.bean.UserBean
import kotlinx.coroutines.CoroutineScope
import javax.security.auth.callback.Callback

/**
 *
 * @Description:
 * @author: bxb
 * @email:baixiaobing@shihuituan.com
 * @Date: 2020/10/15
 */
class LoginPresenter(baseView:BaseView,scope: CoroutineScope): BasePresenter(baseView,scope) {
    /**
     * 登录
     */
    fun login(username:String,password:String,callback:(UserBean)->Unit){
        launchUI {
           val userBean=App.retrofit.create(ServerApi::class.java).login(username,password).check()
            callback(userBean)
        }
    }
}