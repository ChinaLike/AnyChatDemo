package com.tydic.anychatmeeting.model.inf;

/**
 * 作者：like on 2018/6/21 10:04
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：
 */
public interface MicrophoneHelper {
    /**
     * 打开麦克风
     * @param userId
     */
    void openMic(int userId);

    /**
     * 关闭麦克风
     * @param userId
     */
    void closeMic(int userId);

    /**
     * 静音，静音后将无法听到所有人的声音
     * @param mute 是否静音
     */
    void mute(boolean mute);
}
