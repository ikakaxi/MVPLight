package com.liuhc.mvplight.module.home.presenter

import com.liuhc.library.fragment.ext.CommonPagingFragment
import com.liuhc.library.presenter.BasePresenter
import com.liuhc.library.view.base.BaseView
import com.liuhc.mvplight.App
import com.liuhc.mvplight.common.ServerApi
import com.liuhc.mvplight.module.home.bean.BannerBean
import com.liuhc.mvplight.module.home.bean.PackArticleBean
import com.liuhc.mvplight.module.me.bean.CollectBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2020/10/13 on 4:49 PM
 */
class HomePresenter(baseView: BaseView, scope: CoroutineScope) :
    BasePresenter(baseView, scope) {

    private var topArticleListCount = 0

    /**
     * 置顶文章列表和首页文章列表
     */
    fun getTopAndHomeArticleList(page: Int, callback: (CommonPagingFragment.Page<PackArticleBean>) -> Unit) {
        launchUI {
            val topArticleListDeferred = async {
                App.retrofit.create(ServerApi::class.java).getTopArticle().check()
            }
            val homeArticleDeferred = async {
                App.retrofit.create(ServerApi::class.java).getHomeArticle(page).check()
            }
            val list = mutableListOf<PackArticleBean>()
            val topArticleList = topArticleListDeferred.await()
            topArticleListCount = topArticleList.size
            val homeArticle = homeArticleDeferred.await()
            list.addAll(topArticleList.map { PackArticleBean(it, true) })
            list.addAll(homeArticle.datas?.map { PackArticleBean(it) } ?: listOf())
            callback(CommonPagingFragment.Page(1, homeArticle.total + topArticleList.size, list))
        }
    }

    /**
     * 置顶文章列表
     */
    fun getTopArticle(callback: (List<PackArticleBean>) -> Unit) {
        launchUI {
            val topArticleList = App.retrofit.create(ServerApi::class.java).getTopArticle().check()
            callback(topArticleList.map { PackArticleBean(it) })
        }
    }

    /**
     * 首页文章列表
     */
    fun getHomeArticle(page: Int, callback: (CommonPagingFragment.Page<PackArticleBean>) -> Unit) {
        launchUI {
            val homeArticle = App.retrofit.create(ServerApi::class.java).getHomeArticle(page).check()
            callback(
                CommonPagingFragment.Page(
                    homeArticle.curPage,
                    homeArticle.total + topArticleListCount,
                    homeArticle.datas?.map { PackArticleBean(it) })
            )
        }
    }

    /**
     * 首页banner
     */
    fun getBanner(callback: (List<BannerBean>) -> Unit) {
        launchUI {
            val bannerList = App.retrofit.create(ServerApi::class.java).getBanner().check()
            callback(bannerList)
        }
    }

    /**
     * 收藏文章列表
     */
    fun getCollect(page: Int, callback: (CollectBean) -> Unit) {
        launchUI {
            val collectBean = App.retrofit.create(ServerApi::class.java).getCollect(page).check()
            callback(collectBean)
        }
    }

    /**
     * 收藏站内文章
     */
    fun addInSiteCollect(id: Int, callback: (Boolean) -> Unit) {
        launchUI {
            val baseBean = App.retrofit.create(ServerApi::class.java).addInSiteCollect(id)
            callback(baseBean.isSuccess())
        }
    }

    /**
     * 收藏站外文章
     */
    fun addOutSiteCollect(title: String, author: String, link: String, callback: (Boolean) -> Unit) {
        launchUI {
            val baseBean = App.retrofit.create(ServerApi::class.java).addOutSiteCollect(title, author, link)
            callback(baseBean.isSuccess())
        }
    }

    /**
     * 取消收藏(文章列表)
     */
    fun cancelCollect(id: Int, callback: (Boolean) -> Unit) {
        launchUI {
            val baseBean = App.retrofit.create(ServerApi::class.java).cancelCollect(id)
            callback(baseBean.isSuccess())
        }
    }

    /**
     * 取消收藏(我的收藏页面)
     */
    fun cancelCollect(id: String, originId: String, callback: (Boolean) -> Unit) {
        launchUI {
            val baseBean = App.retrofit.create(ServerApi::class.java).cancelCollect(id, originId)
            callback(baseBean.isSuccess())
        }
    }
}