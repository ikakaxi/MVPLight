package com.liuhc.mvplight.module.home.bean

data class HomeArticleBean(
    val curPage: Int,
    val datas: List<ArticleBean>?,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)