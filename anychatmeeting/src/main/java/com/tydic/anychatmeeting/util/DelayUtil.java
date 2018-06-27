package com.tydic.anychatmeeting.util;

import android.os.Handler;

/**
 * 延时帮助类
 * Created by like on 2018/1/17.
 */

public class DelayUtil {
    /**
     * 延时回调
     */
    public interface DelayHelper {
        void onSuccess();
    }

    /**
     * 延时执行
     * @param time
     * @param helper
     */
    public static void delay(long time, final DelayHelper helper) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                helper.onSuccess();
            }
        }, time);
    }

}
