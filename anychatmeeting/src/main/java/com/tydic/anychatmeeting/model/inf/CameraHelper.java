package com.tydic.anychatmeeting.model.inf;

/**
 * 作者：like on 2018/6/21 10:04
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：摄像头控制
 */
public interface CameraHelper {
    /**
     * 打开摄像头
     * @param userId
     */
    void openCamera(int userId);

    /**
     * 关闭摄像头
     * @param userId
     */
    void closeCamera(int userId);

    /**
     * 前置和后置摄像头切换
     */
    void switchCamera();
}
