package com.liuhc.mvplight.module.user.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 *
 * @Description:
 * @author: bxb
 * @Date: 2020/10/15
 */
@Entity(tableName = "users")
data class UserBean(
    @PrimaryKey
    @ColumnInfo(name = "userId") val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "username") val username: String,
    val admin: Boolean?,
    val chapterTops: List<Any>?,
    val coinCount: Int?,
    val collectIds: List<Int>?,
    val email: String?,
    val icon: String?,
    val nickname: String?,
    val password: String?,
    val publicName: String?,
    val token: String?,
    val type: Int?,
)