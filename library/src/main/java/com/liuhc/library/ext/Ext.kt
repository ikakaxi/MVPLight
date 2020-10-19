package com.liuhc.library.ext

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.liuhc.library.R
import com.liuhc.library.glide.GlideApp
import com.liuhc.library.presenter.BasePresenter
import java.util.regex.Pattern

/**
 * 描述:统一类属性/方法扩展文件
 * 作者:liuhaichao
 * 创建日期：2020/8/14 on 6:54 PM
 */

/**
 *  扩展点击事件
 */
fun View.onClick(method: () -> Unit): View {
    setOnClickListener { method() }
    return this
}

private val IMG_URL: Pattern = Pattern.compile(".*?(gif|jpeg|png|jpg|bmp)")
private val VIDEO_URL: Pattern = Pattern.compile(".*?(mp4|3gp|AVI|WMV|rmvb|flv)")

/**
 * 判断一个url是否为图片url
 */
fun String?.isImgUrl(): Boolean {
    return if (this == null || this.trim().isEmpty()) false else IMG_URL.matcher(this).matches()
}

/**
 * 判断一个url是否为视频url
 */
fun String?.isVideoUrl(): Boolean {
    return if (this == null || this.trim().isEmpty()) false else VIDEO_URL.matcher(this).matches()
}

/**
 *  ImageView加载网络图片
 */
fun ImageView.load(url: String?, errorResId: Int = R.drawable.common_load_error) {
    GlideApp.with(context)
        .load(url)
        .placeholder(errorResId)
        .error(errorResId)
        .dontAnimate()
        .into(object : CustomViewTarget<ImageView, Drawable>(this) {
            override fun onLoadFailed(errorDrawable: Drawable?) {
                setImageDrawable(errorDrawable)
            }

            override fun onResourceCleared(placeholder: Drawable?) {

            }

            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                setImageDrawable(resource)
            }

        })
}

/**
 *  扩展视图可见性
 */
fun View.setVisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

@SuppressLint("ObsoleteSdkInt")
private fun getBitmapFormUrl(url: String?, kind: Int): Bitmap? {
    var bitmap: Bitmap? = null
    val retriever = MediaMetadataRetriever()
    try {
        if (Build.VERSION.SDK_INT >= 14) {
            retriever.setDataSource(url, HashMap())
        } else {
            retriever.setDataSource(url)
        }
        /*
        getFrameAtTime()--->在setDataSource()之后调用此方法。
        如果可能，该方法在任何时间位置找到代表性的帧， 并将其作为位图返回。
        这对于生成输入数据源的缩略图很有用。
        */
        bitmap = retriever.frameAtTime
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    } finally {
        try {
            retriever.release()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }
    if (bitmap == null)
        return null
    // Scale down the bitmap if it's too large.
    if (kind == MediaStore.Images.Thumbnails.MINI_KIND) {
        val width = bitmap.width
        val height = bitmap.height
        val max = Math.max(width, height)
        if (max > 512) {
            val scale = 512f / max
            val w = Math.round(scale * width)
            val h = Math.round(scale * height)
            bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true)
        }
    } else if (kind == MediaStore.Images.Thumbnails.MICRO_KIND) {
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, 96, 96, ThumbnailUtils.OPTIONS_RECYCLE_INPUT)
    }
    return bitmap
}

/**
 *  截取视频第一帧,可以获取本地或者网络视频的第一帧,速度较慢,但是获取网络视频的第一帧比loadFirstFrame方法快
 */
fun <T : BasePresenter> loadFirstFrameFromNet(url: String, t: T, callback: (Bitmap) -> Unit) {
    t.launchUI {
        val bitmap = getBitmapFormUrl(url, MediaStore.Images.Thumbnails.MINI_KIND)
        bitmap?.let {
            callback(it)
        }
    }
}

/**
 *  截取视频第一帧,获取本地视频第一帧的速度极快,但是获取网络的视频如果文件比较大会非常慢
 */
fun ImageView.loadFirstFrameFromLocal(
    url: String,
    errorResId: Int = R.drawable.common_load_error,
    requestOptions: RequestOptions.() -> RequestOptions = { this }
) {
    GlideApp.with(context.applicationContext)
        .setDefaultRequestOptions(
            RequestOptions()
                .frame(1000000)
                .centerCrop()
                .apply {
                    error(errorResId)
                    placeholder(errorResId)
                    requestOptions()
                }
        )
        .load(url)
        .into(this)
}