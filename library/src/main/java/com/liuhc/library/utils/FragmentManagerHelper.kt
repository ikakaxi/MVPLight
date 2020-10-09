package com.liuhc.library.utils

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2019-10-13 on 17:55
 */
object FragmentManagerHelper {
    fun addOrSwitch(fragmentManager: FragmentManager,
                    @IdRes containerViewId: Int,
                    fragment: Fragment,
                    tag: String?) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        val childFragments = fragmentManager.fragments
        for (childFragment in childFragments) {
            fragmentTransaction.hide(childFragment!!)
        }
        if (!fragment.isAdded) {
            fragmentTransaction.add(containerViewId, fragment, tag)
        } else {
            fragmentTransaction.show(fragment)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }
}