package com.liuhc.library.utils

import java.lang.reflect.ParameterizedType

/// author:liuhaichao
/// description: 泛型处理类
/// create date：2020-10-13 on 5:36 PM
object TUtils {

    /**
     * 获取泛型实例
     */
    fun <T> getNewInstance(
        any: Any,
        i: Int,
        classArray: Array<Class<*>>,
        initArgsArray: Array<Any>
    ): T {
        return ((any.javaClass
            .genericSuperclass as ParameterizedType).actualTypeArguments[i] as Class<T>)
            .getConstructor(*classArray)
            .newInstance(*initArgsArray)
    }

    /**
     * 获取泛型实例
     */
    fun <T> getNewInstance(any: Any, i: Int): T {
        return ((any.javaClass
            .genericSuperclass as ParameterizedType).actualTypeArguments[i] as Class<T>)
            .newInstance()
    }

    /**
     * 获取泛型类型
     */
    fun <T> getInstance(any: Any, i: Int): T {
        return (any.javaClass
            .genericSuperclass as ParameterizedType)
            .actualTypeArguments[i] as T
    }

    /**
     * 获取泛型类名
     */
    fun getInstanceName(any: Any, i: Int): String {
        return ((any.javaClass
            .genericSuperclass as ParameterizedType)
            .actualTypeArguments[i] as Class<*>).simpleName
    }
}