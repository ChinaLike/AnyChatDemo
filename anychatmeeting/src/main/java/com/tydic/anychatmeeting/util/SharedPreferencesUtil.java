package com.tydic.anychatmeeting.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 作者：like on 2018/5/24 10:44
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：SharedPreferences存储值
 */
public class SharedPreferencesUtil {

    private static SharedPreferences sharedPreferences;

    private SharedPreferencesUtil(){

    }

    public static void init(Context context, String fileName){
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE); //私有数据
    }

    /**
     * 存放整形值
     * @param key
     * @param value
     */
    public static void putInt(String key, int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putInt(key, value);
        editor.commit();//提交修改

    }

    /**
     * 获取字符串
     * @param key
     * @return
     */
    public static String getString(String key){
        return sharedPreferences.getString(key,"");
    }

    /**
     * 存放字符串
     * @param key
     * @param value
     */
    public static void putString(String key, String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString(key, value);
        editor.commit();//提交修改

    }

    /**
     * 获取整形值
     * @param key
     * @return
     */
    public static int getInt(String key){
        return sharedPreferences.getInt(key,0);
    }

    /**
     * 存放布尔型
     * @param key
     * @param value
     */
    public static void putBoolean(String key , boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putBoolean(key, value);
        editor.commit();//提交修改
    }

    /**
     * 获取布尔值
     * @param key
     * @return
     */
    public static boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key,true);
    }

}
