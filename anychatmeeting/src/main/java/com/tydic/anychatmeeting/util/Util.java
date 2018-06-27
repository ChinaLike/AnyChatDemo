package com.tydic.anychatmeeting.util;

import android.content.Context;

/**
 * 工具类的初始化
 * Created by like on 2017/12/27.
 */

public class Util {

    public static void init(Context context){
        T.init(context);
        ConvertUtil.init(context);
    }

}
