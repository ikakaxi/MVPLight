package com.liuhc.mvplight.module.home.fragment

import android.os.Bundle
import android.view.View
import com.liuhc.library.fragment.CommonPagingFragment
import com.liuhc.mvplight.R
import com.liuhc.mvplight.module.home.adapter.ArticleAdapter
import com.liuhc.mvplight.module.home.bean.PackArticleBean
import com.liuhc.mvplight.module.home.presenter.HomePresenter

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2020/10/13 on 4:46 PM
 */
class HomeFragment : CommonPagingFragment<HomePresenter, PackArticleBean, ArticleAdapter>() {

    override fun initViews(view: View, savedInstanceState: Bundle?) {
        super.initViews(view, savedInstanceState)
        mAdapter.setOnItemClickListener { _, _, _ ->

        }
    }

    override fun onGetAdapter(): ArticleAdapter = ArticleAdapter()

    override fun getContentView(): Int = R.layout.common_list

    override fun loadData() {
        if (mIsFirstPage) {
            mPresenter.getTopAndHomeArticleList(mCurrentPage - 1, ::setPageList)
        } else {
            mPresenter.getHomeArticle(mCurrentPage - 1, ::setPageList)
        }
    }

    override fun doGetRootViewBg() = R.color.line

    override fun doGetItemOffsets() = arrayOf(0f, 1f, 0f, 0f)
}