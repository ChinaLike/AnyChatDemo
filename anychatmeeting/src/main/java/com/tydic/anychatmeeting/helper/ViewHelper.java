package com.tydic.anychatmeeting.helper;

/**
 * 作者：like on 2018/5/25 16:39
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：视图帮助类
 */
public interface ViewHelper {

    /**
     * 设置视图的大小
     * @param width
     * @param height
     */
    void size(int width , int height);

    /**
     * 视图的位置
     * @param topMargin
     * @param leftMargin
     */
    void margin(int topMargin, int leftMargin);

    /**
     * 视屏显示的下标
     * @param position
     */
    void position(int position);


}
