package com.liuhc.library.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import java.io.InputStream

/**
 * 描述:
 *  Glide v4 使用注解处理器 (Annotation Processor) 来生成出一个 API,在Application模块中可使用该流式API一次性调用到RequestBuilder,RequestOptions和集成库中所有的选项。
 *  Generated API 模式的设计出于以下两个目的：
 *      1、集成库可以为 Generated API 扩展自定义选项。
 *      2、在 Application 模块中可将常用的选项组打包成一个选项在 Generated API 中使用,虽然以上所说的工作均可以通过手动创建 RequestOptions 子类的方式来完成，但想将它用好更具有挑战，并且降低了 API 使用的流畅性。
 *
 * 参考:
 *  https://www.bzblg.com/article/326.html
 *  https://muyangmin.github.io/glide-docs-cn/doc/configuration.html
 *
 * 作者:liuhaichao
 * 创建日期：2020-10-15 on 3:15 PM
 */
@GlideModule
class LibraryAppGlideModule :AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory())
    }
}