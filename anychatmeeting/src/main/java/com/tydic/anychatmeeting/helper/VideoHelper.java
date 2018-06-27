package com.tydic.anychatmeeting.helper;

/**
 * 作者：like on 2018/5/25 16:41
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：视频帮助类
 */
public interface VideoHelper {

    /**
     * 打开摄像头
     */
    void openCamera();

    /**
     * 关闭摄像头
     */
    void closeCamera();

    /**
     * 没有摄像头
     */
    void noCamera();

    /**
     * 退出视屏
     */
    void exitVideo();
}
