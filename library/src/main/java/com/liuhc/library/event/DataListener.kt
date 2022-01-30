package com.liuhc.library.event

import androidx.annotation.UiThread
import org.jetbrains.annotations.TestOnly
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference

typealias  CallBack<T> = (T) -> Unit

/// author:liuhaichao
/// description: 数据监听类
/// create date: 2020-10-12 on 5:08 PM
object DataListener {

    //事件,"监听者和该事件对应的回调"列表
    private val map: MutableMap<Class<*>, MutableList<CallbackSoftReference>> = mutableMapOf()
    private val queue = ReferenceQueue<Any>()

    /**
     * @param listener 监听者
     * @param eventClass 要监听的类型
     * @param callback 收到事件后会回调该方法
     */
    @Suppress("UNCHECKED_CAST")
    @UiThread
    @JvmStatic
    fun <T> listen(listener: Any, eventClass: Class<T>, callback: CallBack<T>) {
        clearQueue()
        if (!map.containsKey(eventClass)) {
            map[eventClass] = mutableListOf()
        }
        val list = map[eventClass]!!
        val transformCallback: (Any) -> Unit = { callback(it as T) }
        if (!list.contains(listener)) {
            list.add(CallbackSoftReference(listener, transformCallback, queue))
        }
    }

    /**
     * @param event 要发送的事件
     */
    @UiThread
    @JvmStatic
    fun publish(event: Any) {
        clearQueue()
        val eventToCallbackList = map[event::class.java]
        eventToCallbackList?.forEach {
            it.callBack(event)
        }
    }

    /**
     * @param listener 监听者
     * 在页面销毁的时候必须调用,防止内存泄漏,建议写到Activity或者Fragment的onDestroy方法里
     */
    @UiThread
    @JvmStatic
    fun destroy(listener: Any) {
        destroy(listener.hashCode())
    }

    @UiThread
    @JvmStatic
    private fun destroy(listenerHashCode: Int) {
        clearQueue()
        val entryIterator = map.entries.iterator()
        while (entryIterator.hasNext()) {
            val entry = entryIterator.next()
            val list = entry.value
            val listIterator = list.iterator()
            while (listIterator.hasNext()) {
                val callbackSoftReference = listIterator.next()
                if (callbackSoftReference.listenerHashcode == listenerHashCode) {
                    listIterator.remove()
                }
                if (list.isEmpty()) {
                    entryIterator.remove()
                }
            }
        }
    }

    private fun clearQueue() {
        var callbackSoftReference: CallbackSoftReference?
        while (true) {
            callbackSoftReference = queue.poll() as? CallbackSoftReference
            if (callbackSoftReference == null) {
                break
            }
            destroy(callbackSoftReference.listenerHashcode)
        }
    }

    private class CallbackSoftReference(listener: Any, val callBack: CallBack<Any>, queue: ReferenceQueue<Any>) :
        WeakReference<Any>(listener, queue) {
        val listenerHashcode = listener.hashCode()
    }

    @TestOnly
    fun isEmpty(): Boolean {
        clearQueue()
        return map.isEmpty()
    }
}