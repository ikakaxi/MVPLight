package com.liuhc.library.utils

import android.annotation.SuppressLint
import android.app.Application
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.liuhc.library.R

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2020/10/16 on 11:20 AM
 */
@SuppressLint("StaticFieldLeak")
object ToastUtil {
    private lateinit var textView: TextView
    private lateinit var toast: Toast

    /**
     * 在Application里初始化Toast
     */
    fun init(context: Application, @LayoutRes toastLayout: Int) {
        if (!::toast.isInitialized) {
            val rootView = LayoutInflater.from(context).inflate(toastLayout, null)
            textView = rootView.findViewById(R.id.text)
            toast = Toast(context)
            toast.duration = Toast.LENGTH_LONG
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.view = rootView
        }
    }

    /**
     * 显示Toast
     * @param msg 要显示的内容
     * @param duration Toast持续时间
     */
    fun show(msg: CharSequence, duration: Int) {
        if (msg.isNotEmpty()) {
            textView.text = msg
            toast.duration = duration
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }
}