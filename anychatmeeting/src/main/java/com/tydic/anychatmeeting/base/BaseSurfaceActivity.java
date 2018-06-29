package com.tydic.anychatmeeting.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

import com.tydic.anychatmeeting.bean.UsersBean;
import com.tydic.anychatmeeting.constant.Key;
import com.tydic.anychatmeeting.model.AnyChatInit;
import com.tydic.anychatmeeting.model.FloatWindowControl;
import com.tydic.anychatmeeting.model.RequestData;
import com.tydic.anychatmeeting.model.inf.OnItemClickListener;
import com.tydic.anychatmeeting.model.inf.OnRequestListener;
import com.tydic.anychatmeeting.ui.dialog.LayoutSettingPop;
import com.tydic.anychatmeeting.ui.dialog.LoadingDialog;
import com.tydic.anychatmeeting.ui.dialog.MeetingMenuPop;
import com.tydic.anychatmeeting.ui.dialog.OnButtonClickListener;
import com.tydic.anychatmeeting.ui.dialog.OnlineUserPop;
import com.tydic.anychatmeeting.util.ScreenUtil;
import com.tydic.anychatmeeting.util.SharedPreferencesUtil;
import com.tydic.anychatmeeting.widget.MenuLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：like on 2018/6/27 15:19
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：视频显示Activity（包含会议和直播模式）
 */
public abstract class BaseSurfaceActivity extends BaseActivity implements OnRequestListener , MenuLayout.MenuClickListener, OnButtonClickListener,OnItemClickListener {

    protected int anyChatUserId;
    /**
     * 视频的跟布局
     */
    protected RelativeLayout videoRoot;
    /**
     * 会议有关菜单
     */
    protected MeetingMenuPop meetingMenuPop;
    /**
     * 显示加载框
     */
    protected LoadingDialog loadingDialog;
    /**
     * 在线人数
     */
    protected OnlineUserPop onlineUserPop;
    /**
     * 在线人员
     */
    protected List<UsersBean> onLineUserList = new ArrayList<>();

    /**
     * 媒体音量控制
     */
    protected boolean sound;

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        anyChatUserId = SharedPreferencesUtil.getInt(Key.ANYCHAT_USER_ID);
        initPop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开始获取在线人数
        RequestData.onLineUsers(reactBean.getRoomId(), true, this);
    }

    /**
     * 初始化悬浮窗
     */
    protected void initPop() {
        //功能窗
        meetingMenuPop = new MeetingMenuPop(mContext, videoRoot);
        meetingMenuPop.setMenuClickListener(this);

        //进度框
        loadingDialog = new LoadingDialog(mContext);
        //在线人数
        onlineUserPop = new OnlineUserPop(mContext, videoRoot);
        onlineUserPop.setLocationListener(this);
    }

    /**
     * 开始布局
     */
    protected abstract void startLayout();

    @Override
    public void onSuccess(int type, Object obj) {
        switch (type) {
            case Key.ON_LINE_USER:
                //获取在线人数成功
                //清除老数据，设置新数据
                onLineUserList.clear();
                onLineUserList.addAll((List<UsersBean>)obj);
                startLayout();
                break;
            case Key.USER_STATE:
                break;
            default:
                break;
        }
    }

    @Override
    public void onError(int type, int code) {
        switch (type) {
            case Key.ON_LINE_USER:


                break;
            case Key.USER_STATE:
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(int position, UsersBean bean) {

    }

    @Override
    public void changeLocation(int oldPosition, int newPosition) {

    }

    @Override
    public void onVideoMenuClick(int type) {

    }

    @Override
    public void onMenuClick(int type) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AnyChatInit.onDestroy();
    }
}
