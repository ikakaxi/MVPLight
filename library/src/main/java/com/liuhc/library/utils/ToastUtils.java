package com.liuhc.library.utils;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liuhc.library.R;
import com.liuhc.library.activity.BaseApp;

/**
 * Toast 工具类 ,保证用Toast显示消息时只显示一个Toast，且是最新的。
 */
public class ToastUtils {

    /**
     * 单例Toast
     */
//    private static Toast mToast;
    private static int distance = 0;


    /**
     * Toast显示文字，统一设置为显示在底部，稍微向上一些。时间设置为默认的Toast.LENGTH_SHORT
     *
     * @param msg 要显示的文字
     */
    public static void showToast(Context context, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(0, msg);
        }
    }

    public static void showToast(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(0, msg);
        }
    }


    /**
     * 用Toast显示文字，如果当前Toast正在运行，则强制更新显示内容
     *
     * @param msg 显示内容
     */
    public static void showToast(final int redId, final String msg) {
        try {
            if (!TextUtils.isEmpty(msg)) {
                UIHandlerUtils.post(new Runnable() {
                    @Override
                    public void run() {
                        initToast(BaseApp.context, redId, msg);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void initToast(Context context, int imageRedID, String text) {
//        if (mToast == null) {
        Toast mToast = new Toast(context);
//        }
        View v = LayoutInflater.from(context).inflate(R.layout.custom_toast_layout, null);
        TextView tv = v.findViewById(R.id.message);
        ImageView image = v.findViewById(R.id.image);
        LinearLayout layout = v.findViewById(R.id.layout);
        if (imageRedID == 0) {
            image.setVisibility(View.GONE);
        } else {
            image.setVisibility(View.VISIBLE);
            image.setImageResource(imageRedID);
        }
        layout.getBackground().setAlpha(200);
        tv.setText(text);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, distance);
        mToast.setView(v);
        mToast.show();
    }

}
