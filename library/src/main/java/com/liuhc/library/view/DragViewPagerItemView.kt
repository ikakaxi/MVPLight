package com.liuhc.library.view

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import org.jetbrains.annotations.NotNull

/**
 * 描述:DragViewPager中Item的父类,处理一些公共事件
 * 作者:liuhaichao
 * 创建日期：2020/8/28 on 4:32 PM
 */
interface DragViewPagerItemView : LifecycleObserver {

    /**
     * BaseActivity和BaseFragment已经实现了LifecycleOwner接口,
     * 所以在这两个类的子类里调用lifecycle.addObserver就可以让子类具有生命周期监听功能
     *
     * 注意:在BaseActivity或BaseFragment销毁的时候记得调用lifecycle.removeObserver
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(@NotNull lifecycleOwner: LifecycleOwner)

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause(@NotNull lifecycleOwner: LifecycleOwner)

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(@NotNull lifecycleOwner: LifecycleOwner)

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(@NotNull lifecycleOwner: LifecycleOwner)
}