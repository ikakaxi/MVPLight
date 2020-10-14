package com.liuhc.mvplight.module.me.fragment

import android.os.Bundle
import android.view.View
import com.liuhc.library.fragment.BaseMVPFragment
import com.liuhc.mvplight.R
import com.liuhc.mvplight.module.me.presenter.MePresenter

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2020/10/13 on 4:46 PM
 */
class MeFragment : BaseMVPFragment<MePresenter>() {

    override fun loadData() {
    }

    override fun getContentView(): Int = R.layout.fragment_about_me

    override fun initViews(view: View, savedInstanceState: Bundle?) {
    }
}