package com.liuhc.library.ext

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.liuhc.library.R
import com.liuhc.library.glide.GlideApp
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