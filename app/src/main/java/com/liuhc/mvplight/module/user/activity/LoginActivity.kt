package com.liuhc.mvplight.module.user.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.liuhc.library.activity.base.BaseMVPActivity
import com.liuhc.mvplight.MainActivity
import com.liuhc.mvplight.R
import com.liuhc.mvplight.module.user.presenter.UserPresenter
import com.qmuiteam.qmui.kotlin.onClick
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseMVPActivity<UserPresenter>() {
    private var isRegister:Boolean=false;
    override fun loadData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        etUserName.setOnFocusChangeListener { _, hasFocus ->
            ivIconUser.isSelected = hasFocus
        }
        etPassword.setOnFocusChangeListener { _, hasFocus ->
            ivIconPassword.isSelected = hasFocus
        }
        etConfirmPassword.setOnFocusChangeListener { _, hasFocus ->
            ivIconConfirmPassword.isSelected = hasFocus
        }
        tvGoRegister.onClick {
            if(!isRegister){
                isRegister=true
                tvGoRegister.text="去登录"
                llConfirmPassword.visibility= View.VISIBLE
                tvSubmit.text="注册"
                etUserName.text=null
                etPassword.text=null
                etConfirmPassword.text=null
            }else{
                isRegister=false
                tvGoRegister.text="去注册"
                llConfirmPassword.visibility= View.GONE
                tvSubmit.text="登录"
                etUserName.text=null
                etPassword.text=null
            }
        }
        tvSubmit.onClick {
            if (etUserName.text.toString().isNullOrEmpty()) {
                toast("用户名不能为空")
                return@onClick
            }
            if (etPassword.text.toString().isNullOrEmpty()) {
                toast("密码不能为空")
                return@onClick
            }
            if(isRegister){
                if (etConfirmPassword.text.toString().isNullOrEmpty()) {
                    toast("确认密码不能为空")
                    return@onClick
                }
                mPresenter.register(etUserName.text.toString(), etPassword.text.toString(),etConfirmPassword.text.toString()) {
                    toast("注册成功")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }else{
                mPresenter.login(etUserName.text.toString(), etPassword.text.toString()) {
                    toast("登录成功")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_login
    }

}

