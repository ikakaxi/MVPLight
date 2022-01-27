package com.liuhc.library.activity.ext

import android.os.Bundle
import androidx.annotation.CallSuper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.liuhc.library.activity.base.BaseMVPActivity
import com.liuhc.library.fragment.ext.CommonPagingFragment
import com.liuhc.library.presenter.BasePresenter


/**
 * 描述:公共的下拉刷新上拉加载控件,继承该类则不再需要手动更新当前page属性
 * 作者:liuhaichao
 * 创建日期：2020/8/19 on 11:50 AM
 */
abstract class CommonPagingActivity<T : BasePresenter, D, A : BaseQuickAdapter<D, BaseViewHolder>> : BaseMVPActivity<T>() {

    companion object {
        const val PAGING_FRAGMENT_TAG = "PAGING_FRAGMENT_TAG"
    }

    private var innerCommonPagingFragment: InnerCommonPagingFragment? = null

    @Suppress("UNCHECKED_CAST")
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        innerCommonPagingFragment =
            supportFragmentManager.findFragmentByTag(PAGING_FRAGMENT_TAG) as? CommonPagingActivity<T, D, A>.InnerCommonPagingFragment
                ?: InnerCommonPagingFragment()
        showHideFragment(
            innerCommonPagingFragment!!,
            PAGING_FRAGMENT_TAG,
            getFragmentContainerLayoutId()
        )
    }

    abstract fun getFragmentContainerLayoutId(): Int

    abstract fun onGetAdapter(): A

    inner class InnerCommonPagingFragment : CommonPagingFragment<T, D, A>() {

        override fun onGetAdapter() = this@CommonPagingActivity.onGetAdapter()

        override fun getContentView() = this@CommonPagingActivity.getContentView()

        override fun loadData() = this@CommonPagingActivity.loadData()

    }

}