package com.liuhc.mvplight.module.home.presenter

import com.liuhc.library.presenter.BasePresenter
import com.liuhc.library.presenter.view.BaseView
import com.liuhc.mvplight.App
import com.liuhc.mvplight.module.home.bean.HomeBean
import com.liuhc.mvplight.common.ServerApi
import kotlinx.coroutines.CoroutineScope

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2020/10/13 on 4:49 PM
 */
class HomePresenter(baseView: BaseView, scope: CoroutineScope) :
    BasePresenter(baseView, scope) {

    /**
     * 获取首页文章列表
     */
    fun getHome(page: Int, callback: (HomeBean) -> Unit) {
        launchUI {
            val homeBean = App.retrofit.create(ServerApi::class.java).getHomeArticle(page).check()
            callback(homeBean)
        }
    }
}