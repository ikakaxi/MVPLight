package com.liuhc.mvplight

import com.liuhc.library.event.DataListener

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2022/1/26 on 8:03 下午
 */
object Test {
    fun test() {
        Thread {
            while (true) {
                DataListener.listen(ByteArray(1024 * 1024 * 10), Object::class.java) {

                }
                Thread.sleep(1000)
            }
        }.start()
        Thread {
            while (true) {
                System.gc()
                Thread.sleep(5000)
                DataListener.clearQueue()
            }
        }.start()
    }
}