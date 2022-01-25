package com.liuhc.library.event

import androidx.annotation.UiThread
import org.jetbrains.annotations.TestOnly

/// author:liuhaichao
/// description: 数据监听类
/// create date: 2020-10-12 on 5:08 PM
object DataListener {

    //<Event,<Listener,List<CallBack>>>
    private val eventClassToCallbackListMap: MutableMap<Class<*>, MutableMap<Any, MutableList<(Any) -> Unit>>> = mutableMapOf()

    /**
     * @param listener 监听者,传this
     * @param eventClass 要监听的类型
     * @param callback 收到事件后会回调该方法
     */
    @Suppress("UNCHECKED_CAST")
    @UiThread
    @JvmStatic
    fun <T> listen(listener: Any, eventClass: Class<T>, callback: (T) -> Unit) {
        if (!eventClassToCallbackListMap.containsKey(eventClass)) {
            eventClassToCallbackListMap[eventClass] = mutableMapOf()
        }
        val eventToCallbackListMap: MutableMap<Any, MutableList<(Any) -> Unit>>? = eventClassToCallbackListMap[eventClass]
        if (!eventToCallbackListMap!!.containsKey(listener)) {
            eventToCallbackListMap[listener] = mutableListOf()
        }
        val callbackList = eventToCallbackListMap[listener]
        callbackList!!.add {
            callback(it as T)
        }
    }

    @UiThread
    @JvmStatic
    fun publish(any: Any) {
        val eventToCallbackList = eventClassToCallbackListMap[any::class.java]
        eventToCallbackList?.forEach {
            it.value.forEach {
                it(any)
            }
        }
    }

    /**
     * @param listener 监听者,传this
     * 在页面销毁的时候必须调用,防止内存泄漏
     */
    @UiThread
    @JvmStatic
    fun destroy(listener: Any) {
        val eventClassToCallbackListEntryIterator = eventClassToCallbackListMap.entries.iterator()
        while (eventClassToCallbackListEntryIterator.hasNext()) {
            val eventClassToCallbackListEntry = eventClassToCallbackListEntryIterator.next()
            val eventObjectToCallbackListMap = eventClassToCallbackListEntry.value
            val eventObjectToCallbackMapKeysIterator = eventObjectToCallbackListMap.keys.iterator()
            while (eventObjectToCallbackMapKeysIterator.hasNext()) {
                if (eventObjectToCallbackMapKeysIterator.next() == listener) {
                    eventObjectToCallbackMapKeysIterator.remove()
                    //如果某个数据没有监听者了,就把这个数据从map中删掉
                    if (eventObjectToCallbackListMap.isEmpty()) {
                        eventClassToCallbackListEntryIterator.remove()
                    }
                }
            }
        }
    }

    @TestOnly
    fun isEmpty() = eventClassToCallbackListMap.isEmpty()
}