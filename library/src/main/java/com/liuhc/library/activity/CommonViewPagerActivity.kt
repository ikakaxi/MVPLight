package com.liuhc.library.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.liuhc.library.R
import kotlinx.android.synthetic.main.activity_common_viewpager.*

/**
 * 描述:所有有ViewPager的页面都可以继承该类,可自定义xTabLayout的背景颜色
 * 作者:liuhaichao
 * 创建日期：2020/9/15 on 7:02 PM
 */
abstract class CommonViewPagerActivity : BaseActivity() {

    override fun getContentView() = R.layout.activity_common_viewpager

    private lateinit var viewPagerAdapter: CommonViewPagerAdapter

    @CallSuper
    override fun init(savedInstanceState: Bundle?) {
        val titles = doGetTitleArray()
        viewPagerAdapter = CommonViewPagerAdapter(titles, doGetFragmentList(), this)
        viewPager.adapter = viewPagerAdapter
        val tabLayoutMediator = TabLayoutMediator(extTabLayout, viewPager) { tab, position ->
            tab.text = titles[position];
        }
        tabLayoutMediator.attach()
    }

    // 动态设置标题
    protected fun setPageTitle(position: Int, title: String) {
        viewPagerAdapter.setPageTitle(position, title)
    }

    // 设置TabLayout的背景
    protected fun setExtTabLayoutBg(@DrawableRes res: Int) {
        extTabLayout.setBackgroundResource(res)
    }

    class CommonViewPagerAdapter(
        private val titleArray: Array<String>,
        private var fragmentList: Array<Fragment>,
        private val fragmentActivity: FragmentActivity
    ) : FragmentStateAdapter(fragmentActivity) {
        init {
            val firstFragment =
                fragmentActivity.supportFragmentManager.findFragmentByTag("android:switcher:${R.id.viewPager}:0")
            fragmentList = if (firstFragment == null) {
                fragmentList
            } else {
                (fragmentList.indices).asSequence().mapIndexed { index, _ ->
                    fragmentActivity.supportFragmentManager.findFragmentByTag("android:switcher:${R.id.viewPager}:$index")!!
                }.toList().toTypedArray()
            }
        }

        override fun getItemCount(): Int = fragmentList.size

        override fun createFragment(position: Int): Fragment = fragmentList[position]

        // 动态设置标题
        fun setPageTitle(position: Int, title: String) {
            if (position >= 0 && position < titleArray.size) {
                titleArray[position] = title
            }
        }
    }

    abstract fun doGetTitleArray(): Array<String>

    abstract fun doGetFragmentList(): Array<Fragment>

}