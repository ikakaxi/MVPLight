package com.liuhc.library.view

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.lifecycle.LifecycleOwner
import com.liuhc.library.R
import com.liuhc.library.activity.base.BaseMVPActivity
import com.liuhc.library.ext.loadFirstFrameFromNet
import com.liuhc.library.ext.setVisible
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.Debuger
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import kotlinx.android.synthetic.main.common_custom_video.view.*
import org.jetbrains.annotations.NotNull

/**
 * 描述:封装视频播放器
 * 功能:
 *  1.监听Activity生命周期,自动暂停播放和恢复播放
 *  2.如果没有自定义封面,自动显示视频第一帧,否则显示自定义封面(未完成)
 * 作者:liuhaichao
 * 创建日期：2020/8/17 on 1:46 PM
 */
class CustomVideoView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    RelativeLayout(context, attrs, defStyleAttr), DragViewPagerItemView {

    private var isPause = false
    private var isPlay = false
    private lateinit var orientationUtils: OrientationUtils

    init {
        View.inflate(context, R.layout.common_custom_video, this)
    }

    fun initVideoPlayer(videoUrl: String) {
        //外部辅助的旋转，帮助全屏
        orientationUtils = OrientationUtils(context as Activity, videoPlayer)
        //初始化不打开外部的旋转
        orientationUtils.isEnable = false
        videoPlayer.titleTextView.setVisible(false)
        videoPlayer.backButton.setVisible(false)
        //设置全屏按键功能
        videoPlayer.fullscreenButton.setOnClickListener {
            //直接横屏
            orientationUtils.resolveByClick()
            //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusBar
            videoPlayer.startWindowFullscreen(context as Activity, true, true)
        }
        GSYVideoOptionBuilder()
//                .setThumbImageView(getVideoFirstFrame(videoUrl))
            .setIsTouchWiget(true)
            .setRotateViewAuto(false)
            .setLockLand(false)
            .setAutoFullWithSize(true)
            .setShowFullAnimation(false)
            .setNeedLockFull(true)
            .setUrl(videoUrl)
            .setCacheWithPlay(false)
//                .setVideoTitle("测试视频")
            .setVideoAllCallBack(object : GSYSampleCallBack() {

                override fun onPrepared(url: String, vararg objects: Any) {
                    super.onPrepared(url, objects)
                    //开始播放
                    //开始播放了才能旋转和全屏
                    orientationUtils.isEnable = true
                    isPlay = true
                }

                override fun onQuitFullscreen(url: String, vararg objects: Any) {
                    super.onQuitFullscreen(url, objects)
                    Debuger.printfError("***** onQuitFullscreen **** " + objects[0])//title
                    Debuger.printfError("***** onQuitFullscreen **** " + objects[1])//当前非全屏player
                    orientationUtils.backToProtVideo()
                }

            }).setLockClickListener { _, lock ->
                //配合下方的onConfigurationChanged
                orientationUtils.isEnable = !lock
            }.build(videoPlayer)
        loadFirstFrameFromNet(videoUrl, (context as BaseMVPActivity<*>).mPresenter) {
            val coverImageView = ImageView(context)
            coverImageView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            coverImageView.scaleType = ImageView.ScaleType.FIT_CENTER
            coverImageView.setImageBitmap(it)
            videoPlayer.thumbImageView = coverImageView
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            videoPlayer.onConfigurationChanged(context as Activity, newConfig, orientationUtils, true, true)
        }
    }

    /**
     * 退出全屏，主要用于返回键
     *
     * @return 返回是否全屏
     */
    fun isFullScreen(): Boolean {
        orientationUtils.backToProtVideo()
        return GSYVideoManager.backFromWindowFull(context)
    }

    /**
     * 开始播放
     */
    fun start() {
        videoPlayer.startPlayLogic()
    }

    /**
     * 暂停播放
     */
    fun pause() {
        videoPlayer.onVideoPause()
    }

    /**
     * 恢复暂停状态
     */
    override fun onResume(lifecycleOwner: LifecycleOwner) {
        videoPlayer.onVideoResume(false)
        isPause = false
    }

    /**
     * 暂停状态
     */
    override fun onPause(@NotNull lifecycleOwner: LifecycleOwner) {
        pause()
    }

    override fun onStop(@NotNull lifecycleOwner: LifecycleOwner) {
    }

    override fun onDestroy(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.removeObserver(this)
    }

    fun destroy() {
        videoPlayer.release()
        orientationUtils.releaseListener()
    }

    override fun onDetachedFromWindow() {
        destroy()
        super.onDetachedFromWindow()
    }

}