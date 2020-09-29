package com.liuhc.library.activity

import com.liuhc.library.presenter.BasePresenter
import com.liuhc.library.presenter.view.BaseView
import com.trello.rxlifecycle4.LifecycleProvider

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2020/9/29 on 6:57 PM
 */
abstract class BaseMVPActivity<T : BasePresenter>(protected val presenterClass: Class<T>) :
    BaseActivity() {

    //bindUntilEvent<ActivityEvent>(ActivityEvent.DESTROY)

    val mPresenter: T by lazy {
        presenterClass.getConstructor(
            BaseView::class.java,
            LifecycleProvider::class.java
        ).newInstance(this, this)
    }
}