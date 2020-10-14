package com.liuhc.mvplight.module.home.fragment

import com.liuhc.library.fragment.CommonPagingFragment
import com.liuhc.mvplight.R
import com.liuhc.mvplight.module.home.adapter.HomeAdapter
import com.liuhc.mvplight.module.home.bean.HomeBean
import com.liuhc.mvplight.module.home.presenter.HomePresenter

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2020/10/13 on 4:46 PM
 */
class HomeFragment : CommonPagingFragment<HomePresenter, HomeBean.Data, HomeAdapter>() {

    override fun onGetAdapter(): HomeAdapter = HomeAdapter()

    override fun getContentView(): Int = R.layout.common_list

    override fun loadData() {
        mPresenter.getHome(mCurrentPage) {
            setList(Page(it.curPage, it.size, it.datas))
        }
    }

}