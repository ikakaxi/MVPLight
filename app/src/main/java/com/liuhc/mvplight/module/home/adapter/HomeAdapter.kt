package com.liuhc.mvplight.module.home.adapter

import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.liuhc.library.ext.load
import com.liuhc.library.utils.StringUtils
import com.liuhc.mvplight.R
import com.liuhc.mvplight.module.home.bean.TopArticleBean

class HomeAdapter : BaseQuickAdapter<TopArticleBean, BaseViewHolder>(R.layout.item_public) {

    override fun convert(holder: BaseViewHolder, item: TopArticleBean) {
        holder.setGone(R.id.tvNew, !item.fresh)
            .setText(R.id.tvAuthor, item.author)
            .setText(R.id.tvTime, item.niceDate)
            .setText(
                R.id.tvChapterName, Html.fromHtml(
                    formatChapterName(
                        item.superChapterName,
                        item.chapterName
                    )
                )
            )
        holder.getView<TextView>(R.id.tvTitle).apply {
            text = Html.fromHtml(item.title)
            setSingleLine(!TextUtils.isEmpty(item.desc))
        }
        holder.getView<ImageView>(R.id.image).apply {
            if (TextUtils.isEmpty(item.envelopePic)) {
                visibility = View.GONE
            } else {
                load(item.envelopePic)
                visibility = View.VISIBLE
            }
        }
        holder.getView<TextView>(R.id.tvTag).apply {
            if (item.tags.isNotEmpty()) {
                text = item.tags[0].name
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
        }
        holder.getView<TextView>(R.id.tvDesc).apply {
            if (TextUtils.isEmpty(item.desc)) {
                visibility = View.GONE
            } else {
                visibility = View.VISIBLE
                var desc = Html.fromHtml(item.desc).toString()
                desc = StringUtils.removeAllBank(desc, 2)
                text = desc
            }
        }
    }

    private fun formatChapterName(vararg names: String): String {
        val format = StringBuilder()
        for (name in names) {
            if (!TextUtils.isEmpty(name)) {
                if (format.isNotEmpty()) {
                    format.append("·")
                }
                format.append(name)
            }
        }
        return format.toString()
    }
}