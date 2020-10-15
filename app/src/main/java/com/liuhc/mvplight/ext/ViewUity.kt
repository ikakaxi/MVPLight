package com.liuhc.mvplight.ext

import android.content.Context
import android.content.Intent

/**
 *
 * @Description:
 * @author: bxb
 * @email:baixiaobing@shihuituan.com
 * @Date: 2020/10/15
 */
class ViewUity {
    companion object{
        fun startLoginActivty(context:Context,clazz: Class<*>){
          val intent=Intent(context,clazz)
            context.startActivity(intent)
        }
    }
}