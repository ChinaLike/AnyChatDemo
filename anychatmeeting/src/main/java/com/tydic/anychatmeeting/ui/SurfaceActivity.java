package com.tydic.anychatmeeting.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

import com.tydic.anychatmeeting.R;
import com.tydic.anychatmeeting.base.BaseSurfaceActivity;
import com.tydic.anychatmeeting.bean.EventBusBean;
import com.tydic.anychatmeeting.bean.SurfaceConfigBean;
import com.tydic.anychatmeeting.bean.UsersBean;
import com.tydic.anychatmeeting.constant.Key;
import com.tydic.anychatmeeting.model.AnyChatControl;
import com.tydic.anychatmeeting.model.AnyChatLayout;
import com.tydic.anychatmeeting.model.FloatWindowControl;
import com.tydic.anychatmeeting.model.LayoutConfig;
import com.tydic.anychatmeeting.model.LocalVideoControl;
import com.tydic.anychatmeeting.model.RemoteVideoControl;
import com.tydic.anychatmeeting.model.RequestData;
import com.tydic.anychatmeeting.model.SeparateLineLayout;
import com.tydic.anychatmeeting.model.inf.LayoutHelper;
import com.tydic.anychatmeeting.model.inf.OnItemClickListener;
import com.tydic.anychatmeeting.model.inf.OnLayoutChangeListener;
import com.tydic.anychatmeeting.model.inf.OnRequestListener;
import com.tydic.anychatmeeting.ui.dialog.LayoutSettingPop;
import com.tydic.anychatmeeting.ui.dialog.LoadingDialog;
import com.tydic.anychatmeeting.ui.dialog.MeetingMenuPop;
import com.tydic.anychatmeeting.ui.dialog.OnButtonClickListener;
import com.tydic.anychatmeeting.ui.dialog.OnlineUserPop;
import com.tydic.anychatmeeting.util.L;
import com.tydic.anychatmeeting.util.ScreenUtil;
import com.tydic.anychatmeeting.util.SharedPreferencesUtil;
import com.tydic.anychatmeeting.util.StringUtil;
import com.tydic.anychatmeeting.widget.AnyChatView;
import com.tydic.anychatmeeting.widget.MenuLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：like on 2018/5/28 14:12
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：显示直播画面
 */
public class SurfaceActivity extends BaseSurfaceActivity implements MenuLayout.MenuClickListener, OnButtonClickListener,
        OnLayoutChangeListener, OnItemClickListener,LayoutHelper {


    /**
     * 视频功能布局
     */
    private MenuLayout menuLayout;
    /**
     * 布局设置
     */
    protected LayoutSettingPop layoutSettingPop;

    /**
     * 所有布局文件
     */
    private AnyChatLayout anyChatLayout;
    /**
     * 视频、语音
     */
    private AnyChatControl anyChatControl;

    /**
     * 本地视频控件，主要用来显示本地视频
     */
    private AnyChatView localAnyChatView;
    /**
     * 本地视频
     */
    private LocalVideoControl localVideoControl;
    /**
     * 远程视频
     */
    private RemoteVideoControl remoteVideoControl;
    /**
     * 布局配置文件
     */
    private LayoutConfig layoutConfig;
    /**
     * 分割线
     */
    private RelativeLayout separateLineView;


    @Override
    protected int setLayout() {
        return R.layout.activity_surface;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        initPop();
        initLocalVideo();
        remoteVideoControl = new RemoteVideoControl(mContext,videoRoot);
        remoteVideoControl.size(ScreenUtil.getScreenWidth(mContext),ScreenUtil.getScreenHeight(mContext));
        remoteVideoControl.setLayoutHelper(this);
        layoutConfig = new LayoutConfig(mContext);
    }

    @Override
    protected void initPop() {
        super.initPop();
        //布局设置窗
        layoutSettingPop = new LayoutSettingPop(mContext, videoRoot);
        layoutSettingPop.setOnLayoutChangeListener(this);
    }


    @Override
    protected void findIds() {
        videoRoot = findViewById(R.id.video_root);
        menuLayout = findViewById(R.id.video_menu);
        menuLayout.setMenuClickListener(this);
        localAnyChatView = findViewById(R.id.localCamera);
    }

    /**
     * 初始化本地视频
     */
    private void initLocalVideo() {
        localVideoControl = new LocalVideoControl(mContext, localAnyChatView);
        localAnyChatView.init();
        localVideoControl.position(ScreenUtil.getScreenWidth(mContext), ScreenUtil.getScreenHeight(mContext), 0, 0);
        localVideoControl.camera(SharedPreferencesUtil.getInt(Key.LOCAL_USER_CAMERA_KEY));
        localVideoControl.microphone(SharedPreferencesUtil.getInt(Key.LOCAL_USER_MICROPHONE_KEY));
    }

    @Override
    protected void startLayout() {
        if (remoteVideoControl != null){
            remoteVideoControl.showAllRemoteView(onLineUserList,layoutConfig);
        }
    }

    /**
     * 初始化布局文件
     */
    private void initVideoLayout(int width, int height) {
        anyChatLayout = new AnyChatLayout(mContext, width, height);
        anyChatLayout.initLayout(videoRoot, true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //关闭pop
        if (meetingMenuPop.isShowing()) {
            meetingMenuPop.dismiss();
        }
    }

    /**
     * 视频功能控制
     *
     * @param type
     */
    @Override
    public void onMenuClick(int type) {
        switch (type) {
            case MenuLayout.TYPE_TRANSCRIBE:
                //摄像头控制
                anyChatControl.localCamera();
                break;
            case MenuLayout.TYPE_CAMERA:
                //摄像头前后控制
                break;
            case MenuLayout.TYPE_MICROPHONE:
                //本地麦克风控制
                anyChatControl.localMicrophone();
                break;
            case MenuLayout.TYPE_SOUND:
                //远程声音控制
                localVideoControl.mute(sound);
                sound = !sound;
                break;
            case MenuLayout.TYPE_FUN:
                //菜单键
                meetingMenuPop.show();
                break;
            case MenuLayout.TYPE_USER:
                //在线用户查看
                onlineUserPop.show();
                break;
            case MenuLayout.TYPE_SETTING:
                //设置界面布局形式
                layoutSettingPop.show();
                break;
            default:
                break;

        }
    }

    /**
     * 视频设置控制
     *
     * @param type
     */
    @Override
    public void onVideoMenuClick(int type) {
        switch (type) {
            case OnButtonClickListener.MENU_APPOINT_SPEAK:
                //指定发言
                break;
            case OnButtonClickListener.MENU_INVITATION:
                //邀请
                break;
            case OnButtonClickListener.MENU_MEETING_PASSWORD:
                //会议密码
                break;
            case OnButtonClickListener.MENU_MEETING_MATERIAL:
                //会议材料
                break;
            case OnButtonClickListener.MENU_MINI_WINDOW:
                //小窗显示
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制为竖屏
//                floatWindowControl.setOnLineUserList(onLineUserList);
//                floatWindowControl.openFloatWindow();
                finish();
                break;
            case OnButtonClickListener.MENU_EXIT_MEETING:
                //退出会议
                finish();
                break;
            case OnButtonClickListener.MENU_MEETING_BANNER:
                //会议轮播
                break;
            default:
                break;

        }
    }

    /**
     * 更改布局回调
     *
     * @param bean
     */
    @Override
    public void layoutChange(SurfaceConfigBean.LayoutConfigListBean bean) {

        layoutSettingPop.dismiss();
        remoteVideoControl.switchLayout(onLineUserList,layoutConfig);

//        SurfaceConfigBean.LayoutConfigListBean listBean = anyChatLayout.getCell();
//        int userSize = onLineUserList.size();
//        int layoutSize = listBean.getCellInfoList().size();
//        int size = userSize > layoutSize ? layoutSize : userSize;
//        for (int i = 0; i < size; i++) {
//            int userId = onLineUserList.get(i).getUserId();
//            anyChatLayout.resetSize(userId, listBean.getCellInfoList().get(i));
//        }
//        anyChatLayout.setSeparateLine();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void anyChatBus(EventBusBean bean) {
        int dwUserid = bean.dwUserId;
        switch (bean.type) {

            case EventBusBean.ANYCHAT_USER_ENTER_ROOM:
                //用户进入房间
                //刷新在线人数列表
                if (onlineUserPop != null) {
                    onlineUserPop.refresh();
                }
                RequestData.getSpecifiedUserInfo(reactBean.getRoomId(), true, new OnRequestListener() {
                    @Override
                    public void onSuccess(int type, Object obj) {
                        UsersBean dwUserBean = (UsersBean) obj;
                        anyChatControl.enterRoom(dwUserBean);
                    }

                    @Override
                    public void onError(int type, int code) {

                    }
                }, dwUserid);
                break;
            case EventBusBean.ANYCHAT_USER_EXIT_ROOM:
                if (onlineUserPop != null) {
                    onlineUserPop.refresh();
                }
                anyChatControl.exitRoom(dwUserid);
                break;
            case EventBusBean.ANYCHAT_LINK_CLOSE:
                break;

            case EventBusBean.ANYCHAT_CAMERA_STATUS_CHG:
                break;

            case EventBusBean.ANYCHAT_MIC_STATUS_CHG:
                break;
            case EventBusBean.ANYCHAT_FILTER_DATA:
                break;
            case EventBusBean.ANYCHAT_TRANS_BUFFER:
                byte[] lpBuf = bean.lpBuf;
                String msg = StringUtil.byteToString(lpBuf);
                if (msg == null || "".equals(msg) || msg.contains(" ")) {
                    return;
                }
                switch (Integer.parseInt(msg)) {
                    case Key.CLIENT_DISABLE_MIC:
                        //关闭麦克风
//                        menuLayout.initMicStatus(false);
//                        anyChatControl.microphone(false);
                        break;
                    case Key.CLIENT_ENABLE_MIC:
                        //打开麦克风
//                        menuLayout.initMicStatus(true);
//                        anyChatControl.microphone(true);
                        break;
                    case Key.CLIENT_DISABLE_VIDEO:
                        //关闭摄像头
//                        menuLayout.initCameraStatus(false);
//                        anyChatControl.camera(false);
                        break;
                    case Key.CLIENT_ENABLE_VIDEO:
                        //打开摄像头
//                        menuLayout.initCameraStatus(true);
//                        anyChatControl.camera(true);
                        break;
                    default:
                        break;
                }
                //更新状态到服务器
                feedbackState(Key.UPDATE_CLIENT_STATUS, anyChatUserId);
                break;

        }
    }



    @Override
    public void onItemClick(int position, UsersBean bean) {
        onlineUserPop.onLineUser();
    }

    @Override
    public void changeLocation(int oldPosition, int newPosition) {

    }


    @Override
    public void layout(SurfaceConfigBean.LayoutConfigListBean.CellInfoListBean bean) {
        if (bean != null){
            localVideoControl.position((int)(ScreenUtil.getScreenWidth(mContext)*bean.getWidth()+0.5),
                    (int)(ScreenUtil.getScreenHeight(mContext)*bean.getHeight()+0.5),
                    (int)(ScreenUtil.getScreenWidth(mContext)*bean.getTop()+0.5),
                    (int)(ScreenUtil.getScreenHeight(mContext)*bean.getLeft()+0.5));
        }else {
            localVideoControl.position(1,1,0,0);
        }
    }

    @Override
    public void layoutFinish() {
        setSeparateLine();
    }

    /**
     * 设置分割线
     */
    private void setSeparateLine() {
        SeparateLineLayout separateLineLayout = new SeparateLineLayout(layoutConfig.getCurrentLayoutConfig(), mContext);
        separateLineLayout.setSize(ScreenUtil.getScreenWidth(mContext), ScreenUtil.getScreenHeight(mContext));
        if (separateLineView != null) {
            videoRoot.removeView(separateLineView);
        }
        separateLineView = separateLineLayout.drawSeparateLine();
        videoRoot.addView(separateLineView);
    }
}
