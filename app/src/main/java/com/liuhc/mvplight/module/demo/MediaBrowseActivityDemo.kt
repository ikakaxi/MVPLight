package com.liuhc.mvplight.module.demo

import android.os.Bundle
import com.liuhc.library.activity.BaseActivity
import com.liuhc.library.activity.common.MediaBrowseActivity
import com.liuhc.library.ext.onClick
import com.liuhc.library.view.DragViewPager
import com.liuhc.mvplight.R
import kotlinx.android.synthetic.main.activity_media_browser_demo.*
import java.util.*

/**
 * 描述:MediaBrowseActivity测试类
 * 作者:liuhaichao
 * 创建日期：2020/8/17 on 6:10 PM
 */
class MediaBrowseActivityDemo : BaseActivity() {
    override fun init(savedInstanceState: Bundle?) {
        val imgList = ArrayList<DragViewPager.FilesBean>()
        imgList.add(DragViewPager.FilesBean("https://mov.bn.netease.com/open-movie/nos/mp4/2016/01/11/SBC46Q9DV_hd.mp4", true))
        imgList.add(DragViewPager.FilesBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597639470717&di=c60dfb4134f0611eb8fcf61509a7de44&imgtype=0&src=http%3A%2F%2Fa3.att.hudong.com%2F14%2F75%2F01300000164186121366756803686.jpg"))
        imgList.add(DragViewPager.FilesBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597639567209&di=ef1809f206522ea1a79b11851c6827c7&imgtype=0&src=http%3A%2F%2Fa3.att.hudong.com%2F57%2F28%2F01300000921826141405283668131.jpg"))
        imgList.add(DragViewPager.FilesBean("https://mov.bn.netease.com/open-movie/nos/mp4/2016/01/11/SBC46Q9DV_hd.mp4", true))
        imgList.add(DragViewPager.FilesBean("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3238317745,514710292&fm=26&gp=0.jpg"))
        imgList.add(DragViewPager.FilesBean("https://mov.bn.netease.com/open-movie/nos/mp4/2016/01/11/SBC46Q9DV_hd.mp4", true))
        imgList.add(DragViewPager.FilesBean("https://mov.bn.netease.com/open-movie/nos/mp4/2016/01/11/SBC46Q9DV_hd.mp4", true))
        go.onClick {
            MediaBrowseActivity.startActivity(this, imgList, 0, true)
        }
    }

    override fun getContentView() = R.layout.activity_media_browser_demo

}