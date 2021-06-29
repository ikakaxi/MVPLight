package com.liuhc.library.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.liuhc.library.presenter.BasePresenter
import com.liuhc.library.view.base.BaseView
import com.liuhc.library.utils.TUtils
import kotlinx.coroutines.CoroutineScope

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2020/9/29 on 6:57 PM
 */
abstract class BaseMVPFragment<T : BasePresenter>() :
    BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    abstract fun loadData()

    val mPresenter: T by lazy {
        TUtils.getNewInstance(
            any = this,
            i = 0,
            classArray = arrayOf(
                BaseView::class.java,
                CoroutineScope::class.java
            ),
            initArgsArray = arrayOf(this, lifecycleScope)
        )
    }
}