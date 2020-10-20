package com.liuhc.library.activity.common

import android.os.Bundle
import com.liuhc.library.R
import com.liuhc.library.activity.BaseMVPActivity
import com.liuhc.library.ext.onClick
import com.liuhc.library.presenter.BasePresenter
import kotlinx.android.synthetic.main.common_activity_video.*

/**
 * 描述:统一视频播放页
 * 作者:liuhaichao
 * 创建日期：2019-11-27 on 18:35
 */
class VideoActivity : BaseMVPActivity<BasePresenter>() {

    override fun loadData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        backIv.onClick {
            finish()
        }
        val videoUrl = intent.getStringExtra("videoUrl")
        lifecycle.addObserver(customVideoView)
        customVideoView.initVideoPlayer(videoUrl!!)
        customVideoView.start()
    }

    override fun onBackPressedSupport() {
        if (customVideoView.isFullScreen()) {
            return
        }
        super.onBackPressedSupport()
    }

    override fun getContentView(): Int = R.layout.common_activity_video

}