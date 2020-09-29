package com.liuhc.library.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.liuhc.library.fragment.CommonPagingFragment


/**
 * 描述:公共的下拉刷新上拉加载控件,继承该类则不再需要手动更新当前page属性
 * 作者:liuhaichao
 * 创建日期：2020/8/19 on 11:50 AM
 */
abstract class CommonPagingActivity<D, A : BaseQuickAdapter<D, BaseViewHolder>> : BaseActivity() {

    companion object {
        const val PAGING_FRAGMENT_TAG = "PAGING_FRAGMENT_TAG"
    }

    private var innerCommonPagingFragment: InnerCommonPagingFragment? = null

    @Suppress("UNCHECKED_CAST")
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        innerCommonPagingFragment =
            supportFragmentManager.findFragmentByTag(PAGING_FRAGMENT_TAG) as? CommonPagingActivity<D, A>.InnerCommonPagingFragment
                ?: InnerCommonPagingFragment()
        showHideFragment(
            innerCommonPagingFragment!!,
            PAGING_FRAGMENT_TAG,
            getFragmentContainerLayoutId()
        )
    }

    abstract fun getFragmentContainerLayoutId(): Int

    abstract fun onGetAdapter(): A

    inner class InnerCommonPagingFragment : CommonPagingFragment<D, A>() {

        override fun onGetAdapter() = this@CommonPagingActivity.onGetAdapter()

        override fun getContentView() = this@CommonPagingActivity.getContentView()

    }

}