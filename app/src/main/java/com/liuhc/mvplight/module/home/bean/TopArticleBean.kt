package com.liuhc.mvplight.module.home.bean

data class TopArticleBean(
    val children: List<Children>?,
    val courseId: Int?,
    val id: Int?,
    val name: String?,
    val order: Int?,
    val parentChapterId: Int?,
    val userControlSetTop: Boolean?,
    val visible: Int?
) {
    data class Children(
        val courseId: Int?,
        val id: Int?,
        val name: String?,
        val order: Int?,
        val parentChapterId: Int?,
        val userControlSetTop: Boolean?,
        val visible: Int?
    )
}