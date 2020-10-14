package com.liuhc.mvplight.module.home.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.liuhc.mvplight.R
import com.liuhc.mvplight.module.home.bean.HomeBean

class HomeAdapter : BaseQuickAdapter<HomeBean.Data, BaseViewHolder>(R.layout.item_public) {

    override fun convert(holder: BaseViewHolder, item: HomeBean.Data) {
    }

}