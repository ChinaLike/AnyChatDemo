package com.tydic.anychatmeeting.ui.dialog;

/**
 * 作者：like on 2018/6/5 15:59
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：界面上的POP按钮
 */
public interface OnButtonClickListener {
    /**
     * 指定发言
     */
    int MENU_APPOINT_SPEAK = 0;
    /**
     * 邀请
     */
    int MENU_INVITATION = 1;
    /**
     * 会议密码
     */
    int MENU_MEETING_PASSWORD = 2;
    /**
     * 会议材料
     */
    int MENU_MEETING_MATERIAL = 3;
    /**
     * 小窗显示
     */
    int MENU_MINI_WINDOW = 4;
    /**
     * 退出会议
     */
    int MENU_EXIT_MEETING = 5;
    /**
     * 会议轮播
     */
    int MENU_MEETING_BANNER = 6;

    /**
     * 视频功能弹窗
     * @param type
     */
    void onVideoMenuClick(int type);

}
