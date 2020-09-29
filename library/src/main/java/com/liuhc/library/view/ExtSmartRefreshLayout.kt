package com.liuhc.library.view

import android.content.Context
import android.util.AttributeSet
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2019-10-13 on 17:55
 */
class ExtSmartRefreshLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : SmartRefreshLayout(context, attrs) {

    /**
     * 是否开启上拉加载功能
     * true:开启了
     * false:未开启
     */
    fun enableLoadMore() = mEnableLoadMore

}
