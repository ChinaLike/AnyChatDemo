package com.tydic.anychatmeeting.model;

import com.bairuitech.anychat.AnyChatCoreSDK;
import com.tydic.anychatmeeting.bean.UsersBean;
import com.tydic.anychatmeeting.constant.Key;

import java.util.List;

/**
 * 作者：like on 2018/6/29 14:04
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：用户信息
 */
public class UserInfoControl {

    public static final int USERINFO_NAME = 1;
    public static final int USERINFO_IP = 2;

    /**
     * 获取用户昵称
     *
     * @param userId
     * @return
     */
    public String nickName(int userId) {
        return AnyChatCoreSDK.getInstance(null).GetUserInfo(userId, USERINFO_NAME);
    }

    /**
     * 获取主讲人
     * @param list
     * @return
     */
    public UsersBean getSpeaker(List<UsersBean> list){
        for (UsersBean bean :list) {
            if (Key.SPEAKER.equals(bean.getIsPrimarySpeaker())){
                return bean;
            }
        }
        return null;
    }

}
