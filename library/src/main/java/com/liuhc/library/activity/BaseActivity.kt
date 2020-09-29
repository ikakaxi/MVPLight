package com.liuhc.library.activity

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.liuhc.library.utils.FragmentManagerHelper

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2019-10-13 on 17:55
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())
        init(savedInstanceState)
    }

    abstract fun init(savedInstanceState: Bundle?)

    /**
     * 获取布局layout的资源id
     *
     * @return 返回layout的资源id
     */
    abstract fun getContentView(): Int

    fun showHideFragment(fragment: Fragment, tag: String, @IdRes fragmentContainerId: Int) {
        FragmentManagerHelper.addOrSwitch(supportFragmentManager,fragmentContainerId,fragment,tag)
    }

}