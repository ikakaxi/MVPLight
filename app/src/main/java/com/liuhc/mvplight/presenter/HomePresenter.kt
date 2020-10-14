package com.liuhc.mvplight.presenter

import com.liuhc.library.presenter.BasePresenter
import com.liuhc.library.presenter.view.BaseView
import com.liuhc.mvplight.App
import com.liuhc.mvplight.bean.PublicArticle
import com.liuhc.mvplight.net.ServerApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2020/10/13 on 4:49 PM
 */
class HomePresenter(baseView: BaseView, scope: CoroutineScope) :
    BasePresenter(baseView, scope) {

    /**
     * 获取公众号列表
     */
    fun getPublicArticleList(callback: (List<PublicArticle>) -> Unit) {
        launchUI {
            delay(10*1000)
            println("被打印")
            val publicArticleList = App.retrofit.create(ServerApi::class.java).getPublicArticleList().check()
            callback(publicArticleList)
        }
    }
}