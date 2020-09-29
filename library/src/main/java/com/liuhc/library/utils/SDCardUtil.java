package com.liuhc.library.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2019-10-13 on 17:55
 */
public class SDCardUtil {

    private static final String TAG = "SDCardUtils";

    /**
     * 获取app的根目录
     *
     * @return 文件缓存根路径
     */
    public static String getDiskCacheRootDir(Context context) {
        File diskRootFile;
        if (existsSdcard()) {
            diskRootFile = context.getExternalCacheDir();
        } else {
            diskRootFile = context.getCacheDir();
        }
        String cachePath;
        if (diskRootFile != null) {
            cachePath = diskRootFile.getPath();
        } else {
            throw new IllegalArgumentException("disk is invalid");
        }
        return cachePath;
    }

    /**
     * 获取相关功能业务目录
     *
     * @return 文件缓存路径
     */
    public static String getDiskCacheDir(Context context, String dirName) {
        String dir = String.format("%s/%s/", getDiskCacheRootDir(context), dirName);
        File file = new File(dir);
        if (!file.exists()) {
            boolean isSuccess = file.mkdirs();
            if (isSuccess) {
                Log.d(TAG, "dir mkdirs success");
            }
        }
        return file.getPath();
    }

    /**
     * 判断外置sdcard是否可以正常使用
     */
    public static Boolean existsSdcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

}
