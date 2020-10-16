package com.liuhc.library.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.liuhc.library.presenter.view.BaseView
import com.liuhc.library.utils.FragmentManagerHelper
import com.liuhc.library.utils.ToastUtil
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2019-10-13 on 17:55
 */
abstract class BaseFragment : SupportFragment(), BaseView {

    private var loadingDialog: Dialog? = null
    private var mRootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView == null) {
            mRootView = inflater.inflate(getContentView(), null)
            initViews(mRootView!!, savedInstanceState)
        }
        return mRootView
    }

    /**
     * 获取布局layout的资源id
     *
     * @return 返回layout的资源id
     */
    abstract fun getContentView(): Int

    protected abstract fun initViews(view: View, savedInstanceState: Bundle?)

    fun showHideFragment(fragment: Fragment, tag: String, @IdRes fragmentContainerId: Int) {
        FragmentManagerHelper.addOrSwitch(childFragmentManager, fragmentContainerId, fragment, tag)
    }

    /*
     * 显示加载框，默认实现
     */
    override fun showLoading() {
        loadingDialog?.dismiss()
        if (loadingDialog?.isShowing != true) {
            loadingDialog = QMUITipDialog.Builder(requireContext())
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
            loadingDialog = QMUITipDialog.Builder(requireContext())
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
        toast(text, true)
    }

    fun toast(msg: String, long: Boolean = false) {
        ToastUtil.show(msg, if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
    }
}