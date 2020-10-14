package com.liuhc.mvplight.fragment

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.liuhc.library.fragment.CommonPagingFragment
import com.liuhc.mvplight.R
import com.liuhc.mvplight.bean.PublicArticle
import com.liuhc.mvplight.presenter.HomePresenter

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2020/10/13 on 4:46 PM
 */
class HomeFragment : CommonPagingFragment<HomePresenter, PublicArticle, HomeFragment.HomeAdapter>() {

    class HomeAdapter : BaseQuickAdapter<PublicArticle, BaseViewHolder>(R.layout.item_public) {

        override fun convert(holder: BaseViewHolder, item: PublicArticle) {
        }

    }

    override fun onGetAdapter(): HomeAdapter = HomeAdapter()

    override fun getContentView(): Int = R.layout.common_list

    override fun loadData() {
        mPresenter.getPublicArticleList(::setList)
    }

}