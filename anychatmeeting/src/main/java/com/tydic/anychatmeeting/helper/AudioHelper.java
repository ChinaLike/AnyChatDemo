package com.tydic.anychatmeeting.helper;

/**
 * 作者：like on 2018/5/25 16:40
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：音频帮助类
 */
public interface AudioHelper {
    /**
     * 打开音频
     * @param userId
     */
    void openAudio(int userId);

    /**
     * 关闭音频
     * @param userId
     */
    void closeAudio(int userId);
}
