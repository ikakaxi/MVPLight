package com.liuhc.library.presenter

import com.liuhc.library.presenter.view.BaseView
import com.trello.rxlifecycle4.LifecycleProvider

/**
 * ================================================
 * MVP中P层 基类
 * Created by liuhc on 2019/11/8
 * ================================================
 */
open class BasePresenter(val baseView: BaseView, val lifecycleProvider: LifecycleProvider<*>)