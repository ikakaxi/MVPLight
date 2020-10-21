package com.liuhc.library.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView.ScaleType
import android.widget.RelativeLayout
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.liuhc.library.R
import kotlinx.android.synthetic.main.common_custom_photo.view.*

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2020/8/28 on 3:40 PM
 */
class CustomPhotoView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    RelativeLayout(context, attrs, defStyleAttr), DragViewPagerItemView {

    private val lock = Any()

    //是否已经加载成功
    var loaded = false
        private set
    private var loadingAnimator: ObjectAnimator? = null

    init {
        View.inflate(context, R.layout.common_custom_photo, this)
    }

    fun initParams(scaleType: ScaleType) {
        photoView.isEnabled = true
        photoView.scaleType = scaleType
    }

    private fun dismissLoading() {
        loadingImg.visibility = View.GONE
    }

    /**
     * 载入图片成功
     */
    private fun loadSuccess() {
        synchronized(lock) {
            loaded = true
        }
        dismissLoading()
    }

    fun load(url: String) {
        if (!loaded) {
            showLoadingAnimation()
        }
        Glide.with(context)
            .load(url)
            .error(R.drawable.common_load_error)
            .fitCenter()
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    loadSuccess()
                    return false
                }

                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    return false
                }
            })
            .into(photoView)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun showLoadingAnimation() {
        loadingImg.visibility = View.VISIBLE
        if (loadingAnimator == null) {
            loadingAnimator = ObjectAnimator
                .ofFloat(loadingImg, "rotation", 0f, 360f)
                .apply {
                    duration = 2000
                    repeatCount = ValueAnimator.INFINITE
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        setAutoCancel(true)
                    }
                }
        }
        loadingAnimator!!.start()
    }

    private fun stopAnimation() {
        loadingAnimator?.cancel()
        loadingImg.animation?.cancel()
    }

    override fun onResume(lifecycleOwner: LifecycleOwner) {
        if (!loaded) {
            showLoadingAnimation()
        }
    }

    override fun onPause(lifecycleOwner: LifecycleOwner) {
        stopAnimation()
    }

    override fun onStop(lifecycleOwner: LifecycleOwner) {
        stopAnimation()
    }

    override fun onDestroy(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.removeObserver(this)
    }

    fun destroy() {
        //视图销毁后需要重置状态
        synchronized(lock) {
            loaded = false
            if (!loaded) {
                stopAnimation()
            }
        }
    }

    override fun onDetachedFromWindow() {
        destroy()
        super.onDetachedFromWindow()
    }
}