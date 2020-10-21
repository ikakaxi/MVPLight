package com.liuhc.mvplight.module.user.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.liuhc.library.activity.BaseMVPActivity
import com.liuhc.mvplight.MainActivity
import com.liuhc.mvplight.R
import com.liuhc.mvplight.module.user.presenter.LoginPresenter
import com.qmuiteam.qmui.kotlin.onClick
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseMVPActivity<LoginPresenter>() {

    override fun loadData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        etUserName.setOnFocusChangeListener() { v, hasFocus ->
            ivIconUser.isSelected = hasFocus
        }
        etPassword.setOnFocusChangeListener() { v, hasFocus ->
            ivIconPassword.isSelected = hasFocus
        }
        tvLoginSubmit.onClick {
            if (TextUtils.isEmpty(etUserName.text.toString())) {
                toast( "用户名不能为空", false)
                return@onClick
            }
            if (TextUtils.isEmpty(etPassword.text.toString())) {
                toast("密码不能为空",false)
                return@onClick
            }
            mPresenter.login(etUserName.text.toString(), etPassword.text.toString()) {
                toast("登录成功", false)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_login
    }

}

