package com.liuhc.library.activity

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.liuhc.library.presenter.view.BaseView
import com.liuhc.library.utils.FragmentManagerHelper
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2019-10-13 on 17:55
 */
abstract class BaseActivity :
    SupportActivity(),
    BaseView {

    private var loadingDialog: Dialog? = null

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
        FragmentManagerHelper.addOrSwitch(
            supportFragmentManager,
            fragmentContainerId,
            fragment,
            tag
        )
    }

    /*
     * 显示加载框，默认实现
     */
    override fun showLoading() {
        loadingDialog?.dismiss()
        if (loadingDialog?.isShowing != true) {
            loadingDialog = QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create()
            loadingDialog?.show()
        }
    }

    /*
     * 显示加载框，默认实现
     */
    override fun showLoading(text: String) {
        loadingDialog?.dismiss()
        if (loadingDialog?.isShowing != true) {
            loadingDialog = QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(text)
                .create()
            loadingDialog?.show()
        }
    }

    /*
     * 隐藏加载框，默认实现
     */
    override fun hideLoading() {
        if (loadingDialog?.isShowing == true) {
            loadingDialog?.dismiss()
        }
    }

    /*
     * 错误信息提示，默认实现
     */
    override fun onError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}