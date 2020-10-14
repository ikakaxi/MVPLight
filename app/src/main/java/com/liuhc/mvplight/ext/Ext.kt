package com.liuhc.mvplight.ext

import com.liuhc.mvplight.bean.BaseBean

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2020/10/13 on 6:43 PM
 */

fun <T> BaseBean<T>.check(): T {
    if (isSuccess()) {
        return this.data
    } else {
        throw Exception("errorCode=$errorCode,errorMsg=$errorMsg")
    }
}