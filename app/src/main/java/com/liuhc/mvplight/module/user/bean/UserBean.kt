package com.liuhc.mvplight.module.user.bean

/**
 *
 * @Description:
 * @author: bxb
 * @email:baixiaobing@shihuituan.com
 * @Date: 2020/10/15
 */
data class UserBean(
    val admin: Boolean?,
    val chapterTops: List<Any>?,
    val coinCount: Int?,
    val collectIds: List<Int>?,
    val email: String?,
    val icon: String?,
    val id: Int?,
    val nickname: String?,
    val password: String?,
    val publicName: String?,
    val token: String?,
    val type: Int?,
    val username: String?
)