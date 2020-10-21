package com.liuhc.library.event

import androidx.annotation.UiThread
import org.jetbrains.annotations.TestOnly

/**
 * 描述:数据监听类
 * 作者:liuhaichao
 * 创建日期：2020-10-19 on 6:22 PM
 */
object DataListener {

    private val map: MutableMap<Class<*>, MutableMap<Any, MutableList<(Any) -> Unit>>> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    @UiThread
    fun <T> listen(instance: Any, dataClass: Class<T>, callback: (T) -> Unit) {
        var instanceToCallbackList: MutableMap<Any, MutableList<(Any) -> Unit>>? = null
        if (!map.containsKey(dataClass)) {
            map[dataClass] = mutableMapOf()
        }
        instanceToCallbackList = map[dataClass]
        var callbackList: MutableList<(Any) -> Unit>? = null
        if (!instanceToCallbackList!!.containsKey(instance)) {
            instanceToCallbackList[instance] = mutableListOf()
        }
        callbackList = instanceToCallbackList[instance]
        callbackList!!.add {
            callback(it as T)
        }
    }

    fun publish(any: Any) {
        val instanceToCallbackList = map[any::class.java]
        instanceToCallbackList?.forEach {
            it.value.forEach {
                it(any)
            }
        }
    }

    /**
     * 在页面销毁的时候必须调用,防止内存泄漏
     */
    fun destroy(instance: Any) {
        val iterator = map.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val instanceToCallbackMap = entry.value
            val instanceToCallbackMapKeysIterator = instanceToCallbackMap.keys.iterator()
            while (instanceToCallbackMapKeysIterator.hasNext()) {
                if (instanceToCallbackMapKeysIterator.next() == instance) {
                    instanceToCallbackMap.remove(instance)
                    //如果某个数据没有监听者了,就把这个数据从map中删掉
                    if (instanceToCallbackMap.isEmpty()) {
                        map.remove(entry.key)
                    }
                    return
                }
            }
        }
    }

    @TestOnly
    fun isEmpty() = map.isEmpty()
}