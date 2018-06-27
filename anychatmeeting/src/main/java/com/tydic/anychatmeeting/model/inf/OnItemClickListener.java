package com.tydic.anychatmeeting.model.inf;


import com.tydic.anychatmeeting.bean.UsersBean;

/**
 * 在线用户列表Item被点击
 * Created by like on 2017-09-25
 */

public interface OnItemClickListener {
    /**
     * 用户列表被点击
     * @param position  切换前的位置
     * @param bean
     */
    void onItemClick(int position, UsersBean bean);

    /**
     * 开始切换位置
     * @param oldPosition   切换前的位置
     * @param newPosition   切换后的位置
     */
    void changeLocation(int oldPosition, int newPosition);
}
