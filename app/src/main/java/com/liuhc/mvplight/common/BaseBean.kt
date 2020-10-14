package com.liuhc.mvplight.common

/**
 * 描述:
 * errorCode = 0 代表执行成功，不建议依赖任何非0的 errorCode.
 * errorCode = -1001 代表登录失效，需要重新登录。
 * 作者:liuhaichao
 * 创建日期：2020/10/13 on 5:03 PM
 */
class BaseBean<T>(
    val errorCode: Int,
    val errorMsg: String,
    val data: T
) {
    fun isSuccess() = errorCode == 0

    fun check(): T {
        if (isSuccess()) {
            return data
        } else {
            throw Exception("errorCode=$errorCode,errorMsg=$errorMsg")
        }
    }
}