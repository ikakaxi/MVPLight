package com.liuhc.mvplight.net

import com.liuhc.mvplight.bean.BaseBean
import com.liuhc.mvplight.bean.PublicArticle
import retrofit2.http.GET

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2020/10/13 on 4:51 PM
 */
interface ServerApi {

    /**
     * 获取公众号列表
     */
    @GET("/wxarticle/chapters/json")
    suspend fun getPublicArticleList(): BaseBean<List<PublicArticle>>
}