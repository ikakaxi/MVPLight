package com.liuhc.library.activity.common

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.liuhc.library.R
import com.liuhc.library.activity.base.BaseMVPActivity
import com.liuhc.library.event.DataListener
import com.liuhc.library.event.msg.DeletePictureMsg
import com.liuhc.library.ext.onClick
import com.liuhc.library.presenter.BasePresenter
import com.liuhc.library.view.DragViewPager
import kotlinx.android.synthetic.main.common_activity_media_browser.*
import java.util.*

/**
 * 描述:浏览图片和视频的页面
 * 作者:liuhaichao
 * 创建日期：2020/8/14 on 6:34 PM
 *
 * Demo:MediaBrowseActivityDemo
 */
class MediaBrowseActivity : BaseMVPActivity<BasePresenter>() {

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        val mediaFileList = intent.getSerializableExtra(DATA) as ArrayList<DragViewPager.FilesBean>
        val curPosition = savedInstanceState?.getInt(POSITION, 0) ?: intent.getIntExtra(POSITION, 0)
        mViewPager.initParams(mediaFileList, curPosition)
        if (intent.getBooleanExtra(SHOW_DELETE, false)) {
            deleteIv.visibility = View.VISIBLE
            deleteIv.onClick {
                mediaFileList.removeAt(mViewPager.currentItem)
                DataListener.publish(DeletePictureMsg(mViewPager.currentItem))
                if (mediaFileList.size == 0) {
                    mViewPager.finishViewPager()
                } else {
                    mViewPager.adapter?.notifyDataSetChanged()
                    photoOrderTv.text = "${mViewPager.currentItem + 1}/${mediaFileList.size}"
                }
            }
        }
        closeIv.onClick {
            mViewPager.finishViewPager()
        }
        mViewPager.pageMargin = (resources.displayMetrics.density * 15).toInt()
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                //设置页面的编号
                photoOrderTv.text = "${position + 1}/${mediaFileList.size}"
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }

    companion object {
        //当前预览图片位置
        const val POSITION = "position"

        //是否显示删除按钮
        const val SHOW_DELETE = "showDelete"

        //数据
        const val DATA = "data"

        fun startActivity(
            context: Activity,
            filesBeans: ArrayList<DragViewPager.FilesBean>,
            position: Int,
            showDelete: Boolean
        ) {
            val intent = Intent(context, MediaBrowseActivity::class.java)
            intent.putExtra(POSITION, position)
            intent.putExtra(DATA, filesBeans)
            intent.putExtra(SHOW_DELETE, showDelete)
            context.startActivity(intent)
            context.overridePendingTransition(0, 0)
        }
    }

    override fun getContentView() = R.layout.common_activity_media_browser

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(POSITION, mViewPager.currentItem)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressedSupport() {
        mViewPager.finishViewPager()
    }

    override fun onDestroy() {
        mViewPager.destroy()
        super.onDestroy()
    }

    override fun loadData() {
    }
}