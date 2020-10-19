package com.liuhc.library.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkUtils {

    /**
     * 对网络连接状态进行判断
     * @return  true, 可用； false， 不可用
     */
    public static boolean isOpenNetwork(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if(networkInfo!= null) {
                //2.获取当前网络连接的类型信息
                int networkType = networkInfo.getType();
                if(ConnectivityManager.TYPE_WIFI == networkType){
                    //当前为wifi网络
                }else if(ConnectivityManager.TYPE_MOBILE == networkType){
                    //当前为mobile网络
                }
                return connManager.getActiveNetworkInfo().isAvailable();
            }
        }catch (Exception e){
            return false;
        }

        return false;
    }

    /**
     * 判断是否链接wifi
     * @param context
     * @return
     */
    public static boolean isWifiState(Context context){
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if(networkInfo!= null) {
                //2.获取当前网络连接的类型信息
                int networkType = networkInfo.getType();
                if(ConnectivityManager.TYPE_WIFI == networkType){
                    return true;
                }else if(ConnectivityManager.TYPE_MOBILE == networkType){
                    return false;
                }
                return connManager.getActiveNetworkInfo().isAvailable();
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }
}
