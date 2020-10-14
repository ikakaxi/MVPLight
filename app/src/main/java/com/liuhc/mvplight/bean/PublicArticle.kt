package com.liuhc.mvplight.bean

/**
 * 公众号
 */
data class PublicArticle(
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)