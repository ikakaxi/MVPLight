package com.liuhc.mvplight.module.home.fragment

import android.os.Bundle
import android.view.View
import com.liuhc.library.fragment.CommonPagingFragment
import com.liuhc.mvplight.R
import com.liuhc.mvplight.module.home.adapter.HomeAdapter
import com.liuhc.mvplight.module.home.bean.TopArticleBean
import com.liuhc.mvplight.module.home.presenter.HomePresenter

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2020/10/13 on 4:46 PM
 */
class HomeFragment : CommonPagingFragment<HomePresenter, TopArticleBean, HomeAdapter>() {

    override fun initViews(view: View, savedInstanceState: Bundle?) {
        super.initViews(view, savedInstanceState)
        mAdapter.setOnItemClickListener { _, _, _ ->

        }
    }

    override fun onGetAdapter(): HomeAdapter = HomeAdapter()

    override fun getContentView(): Int = R.layout.common_list

    override fun loadData() {
        mPresenter.getTopArticle(::setList)
    }

    override fun doGetItemOffsets() = arrayOf(0f, 1f, 0f, 0f)
}