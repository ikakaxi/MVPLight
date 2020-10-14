package com.liuhc.mvplight.common

import com.liuhc.mvplight.module.home.bean.BannerBean
import com.liuhc.mvplight.module.home.bean.HomeBean
import com.liuhc.mvplight.module.home.bean.TopArticleBean
import com.liuhc.mvplight.module.me.bean.CollectBean
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2020/10/13 on 4:51 PM
 */
interface ServerApi {

    /**
     * 置顶文章
     */
    @GET("article/top/json")
    suspend fun getTopArticle(): BaseBean<List<TopArticleBean>>

    /**
     * 首页文章列表
     */
    @GET("/article/list/{page}/json")
    suspend fun getHomeArticle(@Path("page") page: Int): BaseBean<HomeBean>

    /**
     * 首页banner
     */
    @GET("/banner/json")
    suspend fun getBanner(): BaseBean<List<BannerBean>>

    /**
     * 收藏文章列表
     */
    @GET("/lg/collect/list/{page}/json")
    suspend fun getCollect(@Path("page") page: Int): BaseBean<CollectBean>

    /**
     * 收藏站内文章
     */
    @POST("/lg/collect/{id}/json")
    suspend fun addInSiteCollect(@Path("id") id: Int): BaseBean<*>

    /**
     * 收藏站外文章
     */
    @POST("/lg/collect/add/json")
    suspend fun addOutSiteCollect(@Field("title") title: String, @Field("author") author: String, @Field("link") link: String): BaseBean<*>

    /**
     * 取消收藏(文章列表)
     */
    @POST("/lg/uncollect_originId/{id}/json")
    suspend fun cancelCollect(@Path("id") id: Int): BaseBean<HomeBean>

    /**
     * 取消收藏(我的收藏页面)
     */
    @POST("/lg/uncollect/{id}/json")
    suspend fun cancelCollect(@Path("id") id: String, @Field("originId") originId: String): BaseBean<*>
}