package com.liuhc.mvplight

import android.os.Bundle
//import com.flyco.tablayout.listener.CustomTabEntity
//import com.flyco.tablayout.listener.OnTabSelectListener
import com.liuhc.library.activity.base.BaseActivity
import com.liuhc.mvplight.module.home.fragment.HomeFragment
import com.liuhc.mvplight.module.me.fragment.MeFragment
import me.yokeyword.fragmentation.ISupportFragment

class MainActivity : BaseActivity() {

    private val mTitles = arrayOf("首页", "我的")
    private val mIconUnSelectIds = intArrayOf(R.drawable.tab_home_normal, R.drawable.tab_user_normal)
    private val mIconSelectIds = intArrayOf(R.drawable.tab_home_press, R.drawable.tab_user_press)
    private val mFragments = mutableListOf<ISupportFragment>()

    override fun init(savedInstanceState: Bundle?) {
        initBottomNav()
        initFragment()
    }

    override fun getContentView() = R.layout.activity_main

    /**
     *  初始化底部导航切换事件
     */
    private fun initBottomNav() {
//        bottomNavBar.setTabData(ArrayList(mTitles.mapIndexed { index, s ->
//            TabEntity(s, mIconSelectIds[index], mIconUnSelectIds[index])
//        }))
//        bottomNavBar.setOnTabSelectListener(object : OnTabSelectListener {
//            override fun onTabSelect(position: Int) {
//                showHideFragment(mFragments[position])
//            }
//
//            override fun onTabReselect(position: Int) {
//            }
//        })

    }

    /**
     * 初始化Fragment
     */
    private fun initFragment() {
        //首页
        var homeFragment: ISupportFragment? = null
        //我的
        var aboutMeFragment: ISupportFragment? = null

        homeFragment = findFragment(HomeFragment::class.java)
        aboutMeFragment = findFragment(MeFragment::class.java)
        if (homeFragment == null) {
            mFragments.add(HomeFragment())
            mFragments.add(MeFragment())

            loadMultipleRootFragment(
                R.id.fragmentContainer, 0,
                mFragments[0],
                mFragments[1]
            )
        } else {
            mFragments.add(homeFragment)
            mFragments.add(aboutMeFragment)
        }
        switchTab(0)
    }

    private fun switchTab(position: Int) {
//        bottomNavBar.currentTab = position
        //上面的代码并没有调用OnTabSelectListener的方法，所以加上下面的代码解决该第三方框架的bug
        showHideFragment(mFragments[position])
    }

//    private class TabEntity(private val title: String, private val selectedIcon: Int, private val unSelectedIcon: Int) : CustomTabEntity {
//
//        override fun getTabUnselectedIcon(): Int = unSelectedIcon
//
//        override fun getTabSelectedIcon(): Int = selectedIcon
//
//        override fun getTabTitle(): String = title
//    }
}