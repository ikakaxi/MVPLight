package com.liuhc.mvplight.module.user.activity

import android.os.Bundle
import android.os.UserHandle
import android.text.TextUtils
import android.widget.TextView
import com.liuhc.library.activity.BaseMVPActivity
import com.liuhc.library.utils.ToastUtils
import com.liuhc.mvplight.MainActivity
import com.liuhc.mvplight.R
import com.liuhc.mvplight.ext.ViewUity
import com.liuhc.mvplight.module.user.bean.UserBean
import com.liuhc.mvplight.module.user.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseMVPActivity<LoginPresenter>() {
    override fun loadData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        tvLoginSubmit.setOnClickListener()
    }

    override fun getContentView(): Int {
        return R.layout.activity_login
    }

    fun TextView.setOnClickListener() {
        if (TextUtils.isEmpty(etUserName.text.toString())) {
            ToastUtils.showToast("用户名不能为空")
            return
        }
        if (TextUtils.isEmpty(etPassword.text.toString())) {
            ToastUtils.showToast("密码不能为空")
            return
        }
        mPresenter.login(etUserName.text.toString(),etPassword.text.toString()){userBean:UserBean->
            ToastUtils.showToast("登录成功")
           ViewUity.startLoginActivty(this@LoginActivity,MainActivity::class.java)
        }
    }
}

