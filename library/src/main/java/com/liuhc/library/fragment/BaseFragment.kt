package com.liuhc.library.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.liuhc.library.utils.FragmentManagerHelper
import me.yokeyword.fragmentation.SupportFragment

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2019-10-13 on 17:55
 */
abstract class BaseFragment : SupportFragment() {

    private var mRootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView == null) {
            mRootView = inflater.inflate(getContentView(), null)
            initViews(mRootView!!, savedInstanceState)
        }
        return mRootView
    }

    /**
     * 获取布局layout的资源id
     *
     * @return 返回layout的资源id
     */
    abstract fun getContentView(): Int

    protected abstract fun initViews(view: View, savedInstanceState: Bundle?)

    open fun loadData() {

    }

    fun showHideFragment(fragment: Fragment, tag: String, @IdRes fragmentContainerId: Int) {
        FragmentManagerHelper.addOrSwitch(childFragmentManager, fragmentContainerId, fragment, tag)
    }
}