package com.liuhc.library.utils;

import android.os.Handler;
import android.os.Looper;

/**
 */
public class UIHandlerUtils {

    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public static Handler get() {
        return mHandler;
    }

    /**
     * async
     * @param r
     */
    public static void post(Runnable r) {
        if (Looper.myLooper() == mHandler.getLooper()) {
            r.run();
        } else {
            mHandler.post(r);
        }
    }

    /**
     * sync execute
     * @param r
     */
    public static void execute(final Runnable r) {
        if (Looper.myLooper() == mHandler.getLooper()) {
            r.run();
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    synchronized (r) {
                        r.run();
                        r.notify();
                    }
                }
            });

            synchronized (r) {
                try {
                    r.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
