package com.tydic.anychatmeeting.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import com.tydic.anychatmeeting.bean.EventBusBean;
import com.tydic.anychatmeeting.bean.UsersBean;
import com.tydic.anychatmeeting.constant.Key;
import com.tydic.anychatmeeting.model.AnyChatInit;
import com.tydic.anychatmeeting.model.LocalVideoControl;
import com.tydic.anychatmeeting.model.RequestData;
import com.tydic.anychatmeeting.model.UserInfoControl;
import com.tydic.anychatmeeting.model.VideoStatusControl;
import com.tydic.anychatmeeting.model.inf.OnItemClickListener;
import com.tydic.anychatmeeting.model.inf.OnRequestListener;
import com.tydic.anychatmeeting.ui.dialog.LoadingDialog;
import com.tydic.anychatmeeting.ui.dialog.MeetingMenuPop;
import com.tydic.anychatmeeting.ui.dialog.MeetingMeterialsPop;
import com.tydic.anychatmeeting.ui.dialog.OnButtonClickListener;
import com.tydic.anychatmeeting.ui.dialog.OnlineUserPop;
import com.tydic.anychatmeeting.util.L;
import com.tydic.anychatmeeting.util.ScreenUtil;
import com.tydic.anychatmeeting.util.SharedPreferencesUtil;
import com.tydic.anychatmeeting.util.StringUtil;
import com.tydic.anychatmeeting.util.T;
import com.tydic.anychatmeeting.widget.AnyChatView;
import com.tydic.anychatmeeting.widget.MenuLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：like on 2018/6/27 15:19
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：视频显示Activity（包含会议和直播模式）
 */
public abstract class BaseSurfaceActivity extends BaseActivity implements OnRequestListener, MenuLayout.MenuClickListener, OnButtonClickListener, OnItemClickListener {
    /**
     * 用户ID（在anychat中的ID）
     */
    protected int anyChatUserId;
    /**
     * 视频的跟布局
     */
    protected RelativeLayout videoRoot;
    /**
     * 视频功能布局
     */
    protected MenuLayout menuLayout;
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
     * 会议材料
     */
    protected MeetingMeterialsPop meetingMeterialsPop;

    /**
     * 在线人员
     */
    protected List<UsersBean> onLineUserList = new ArrayList<>();
    /**
     * 媒体音量控制
     */
    protected boolean sound;
    /**
     * 音视频状态控制
     */
    private VideoStatusControl videoStatusControl;
    /**
     * 本地视频控件，主要用来显示本地视频
     */
    protected AnyChatView localAnyChatView;
    /**
     * 本地视频
     */
    protected LocalVideoControl localVideoControl;
    /**
     * 用户信息
     */
    protected UserInfoControl userInfoControl;

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        anyChatUserId = SharedPreferencesUtil.getInt(Key.ANYCHAT_USER_ID);
        videoStatusControl = new VideoStatusControl(mContext);
        userInfoControl = new UserInfoControl();
        initLocalVideo();
        initPop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开始获取在线人数
        RequestData.onLineUsers(reactBean.getRoomId(), true, this);
    }

    /**
     * 初始化本地视频
     */
    protected void initLocalVideo() {
        localVideoControl = new LocalVideoControl(mContext, localAnyChatView);
        localAnyChatView.init();
        if (reactBean.getMode() == Key.MODE_ONLIVE){
            localVideoControl.position(1,1, 0, 0);
        }else {
            localVideoControl.position(ScreenUtil.getScreenWidth(mContext), ScreenUtil.getScreenHeight(mContext), 0, 0);
        }
        localVideoControl.camera(SharedPreferencesUtil.getInt(Key.LOCAL_USER_CAMERA_KEY));
        localVideoControl.microphone(SharedPreferencesUtil.getInt(Key.LOCAL_USER_MICROPHONE_KEY));
        localVideoControl.setNickName(anyChatUserId);
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
        onlineUserPop.setReactBean(reactBean);
        onlineUserPop.setLocationListener(this);
        meetingMeterialsPop = new MeetingMeterialsPop(mContext, videoRoot);
        meetingMeterialsPop.setUserId(reactBean.getUserId());
        meetingMeterialsPop.setToken(reactBean.getToken());
        meetingMeterialsPop.getMaterials(reactBean.getMeetingId());
    }

    /**
     * 开始布局
     */
    protected abstract void startLayout();


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
                meetingMeterialsPop.show();
                break;
            case OnButtonClickListener.MENU_MINI_WINDOW:
                //小窗显示
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

    @Override
    public void onSuccess(int type, Object obj) {
        switch (type) {
            case Key.ON_LINE_USER:
                //获取在线人数成功
                //清除老数据，设置新数据
                onLineUserList.clear();
                onLineUserList.addAll((List<UsersBean>) obj);
                UsersBean speakerBean = userInfoControl.getSpeaker(onLineUserList);
                if (speakerBean == null) {
                    //1.普通模式下，显示所有人视频，2.直播模式下，没有主讲人将等待主讲人进入
                    startLayout();
                } else {
                    setSpeaker(speakerBean);
                }

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
                int cameraStatus = SharedPreferencesUtil.getInt(Key.LOCAL_USER_CAMERA_KEY);
                if (cameraStatus == Key.CAMERA_OPEN) {
                    SharedPreferencesUtil.putInt(Key.LOCAL_USER_CAMERA_KEY, Key.CAMERA_CLOSE);
                } else if (cameraStatus == Key.CAMERA_CLOSE) {
                    SharedPreferencesUtil.putInt(Key.LOCAL_USER_CAMERA_KEY, Key.CAMERA_OPEN);
                }
                localVideoControl.camera(SharedPreferencesUtil.getInt(Key.LOCAL_USER_CAMERA_KEY));
                break;
            case MenuLayout.TYPE_CAMERA:
                //前后摄像头控制
                videoStatusControl.switchCamera();
                break;
            case MenuLayout.TYPE_MICROPHONE:
                //本地麦克风控制
                int micStatus = SharedPreferencesUtil.getInt(Key.LOCAL_USER_MICROPHONE_KEY);
                if (micStatus == Key.MIC_OPEN) {
                    SharedPreferencesUtil.putInt(Key.LOCAL_USER_MICROPHONE_KEY, Key.MIC_CLOSE);
                } else {
                    SharedPreferencesUtil.putInt(Key.LOCAL_USER_MICROPHONE_KEY, Key.MIC_OPEN);
                }
                localVideoControl.microphone(SharedPreferencesUtil.getInt(Key.LOCAL_USER_MICROPHONE_KEY));
                break;
            case MenuLayout.TYPE_SOUND:
                //远程声音控制
                int soundStatus = SharedPreferencesUtil.getInt(Key.LOCAL_USER_SOUND_KEY);
                if (soundStatus == Key.SOUND_OPEN) {
                    SharedPreferencesUtil.putInt(Key.LOCAL_USER_SOUND_KEY, Key.SOUND_CLOSE);
                    sound = false;
                } else {
                    SharedPreferencesUtil.putInt(Key.LOCAL_USER_SOUND_KEY, Key.SOUND_OPEN);
                    sound = true;
                }
                videoStatusControl.mute(sound);
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
                if (reactBean.getMode() == Key.MODE_ONLIVE){
                    T.showShort("直播模式下无法改变布局");
                }else {
                    showLayoutSetting();
                }
                break;
            default:
                break;

        }
    }

    /**
     * 接受用户信息，并作出相应的处理
     *
     * @param bean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void anyChatBus(EventBusBean bean) {
        final int dwUserid = bean.dwUserId;
        switch (bean.type) {
            case EventBusBean.ANYCHAT_USER_ENTER_ROOM:
                //用户进入房间时，刷新用户列表
                if (onlineUserPop != null) {
                    onlineUserPop.refresh();
                }
                /**
                 * 获取进入房间用户完整信息
                 */
                RequestData.getSpecifiedUserInfo(reactBean.getRoomId(), true, new OnRequestListener() {
                    @Override
                    public void onSuccess(int type, Object obj) {
                        T.showShort(userInfoControl.nickName(dwUserid) + "进入房间了！");
                        UsersBean dwUserBean = (UsersBean) obj;
                        //添加数据
                        onLineUserList.add(dwUserBean);
                        //刷新界面
                        enterRoom(dwUserBean);
                    }

                    @Override
                    public void onError(int type, int code) {

                    }
                }, dwUserid);
                break;
            case EventBusBean.ANYCHAT_USER_EXIT_ROOM:
                //用户退出房间时，刷新用户列表
                if (onlineUserPop != null) {
                    onlineUserPop.refresh();
                }
                T.showShort(userInfoControl.nickName(dwUserid) + "退出房间了！");
                //删除数据
                UsersBean.removeUser(onLineUserList, dwUserid);
                //刷新界面
                exitRoom(dwUserid);
                break;
            case EventBusBean.ANYCHAT_LINK_CLOSE:
                break;

            case EventBusBean.ANYCHAT_CAMERA_STATUS_CHG:
                //摄像头状态改变
                if (dwUserid != anyChatUserId) {
                    remoteCamera(dwUserid, bean.dwState);
                }
                break;

            case EventBusBean.ANYCHAT_MIC_STATUS_CHG:
                //麦克风状态改变
                if (dwUserid != anyChatUserId) {
                    remoteMic(dwUserid, bean.dwState);
                }
                break;
            case EventBusBean.ANYCHAT_FILTER_DATA:
                //设置主讲人
                String receiveMsg = StringUtil.byteToString(bean.lpBuf);
                if (receiveMsg == null) {
                    return;
                }
                L.d("消息通知", receiveMsg);
                String[] arr = receiveMsg.split(" ");
                int speakerId = Integer.parseInt(arr[1]);
                if (Integer.parseInt(arr[0]) == Key.CLIENT_NOTICE_PRIMARY_SPEAKER) {
                    //主讲人操作
                    if (speakerId != 0) {
                        RequestData.getSpecifiedUserInfo(reactBean.getRoomId(), false, new OnRequestListener() {
                            @Override
                            public void onSuccess(int type, Object obj) {
                                setSpeaker((UsersBean) obj);
                            }

                            @Override
                            public void onError(int type, int code) {
                                T.showShort("获取主讲人信息失败");
                            }
                        }, speakerId);
                    } else {
                        cancelSpeaker();
                    }
                }
                break;
            case EventBusBean.ANYCHAT_TRANS_BUFFER:
                String flag = StringUtil.byteToString(bean.lpBuf);
                if (flag == null || "".equals(flag) || flag.contains(" ")) {
                    return;
                }
                L.d("消息通知2", flag);
                switch (Integer.parseInt(flag)) {
                    case Key.CLIENT_DISABLE_MIC:
                        //关闭麦克风
                        menuLayout.performClick(MenuLayout.TYPE_MICROPHONE);
                        break;
                    case Key.CLIENT_ENABLE_MIC:
                        //打开麦克风
                        menuLayout.performClick(MenuLayout.TYPE_MICROPHONE);
                        break;
                    case Key.CLIENT_DISABLE_VIDEO:
                        //关闭摄像头
                        menuLayout.performClick(MenuLayout.TYPE_TRANSCRIBE);
                        break;
                    case Key.CLIENT_ENABLE_VIDEO:
                        //打开摄像头
                        menuLayout.performClick(MenuLayout.TYPE_TRANSCRIBE);
                        break;
                    default:
                        break;
                }
//                //更新状态到服务器
                feedbackState(Key.UPDATE_CLIENT_STATUS, anyChatUserId);
                break;

        }
    }

    /**
     * 进入房间
     *
     * @param enterUserBean
     */
    protected abstract void enterRoom(UsersBean enterUserBean) ;

    /**
     * 退出房间
     *
     * @param exitUserid
     */
    protected abstract void exitRoom(int exitUserid) ;

    /**
     * 摄像头控制
     *
     * @param userId
     * @param cameraStatus
     */
    protected abstract void remoteCamera(int userId, int cameraStatus);

    /**
     * 摄像头控制
     * @param userId
     * @param micStatus
     */
    protected abstract void remoteMic(int userId , int micStatus);

    /**
     * 设置主讲人，仅在普通视频模式下有
     *
     * @param bean
     */
    protected void setSpeaker(UsersBean bean) {

    }

    /**
     * 取消主讲人
     */
    protected void cancelSpeaker() {

    }

    /**
     * 显示布局切换设置
     */
    protected abstract void showLayoutSetting();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AnyChatInit.onDestroy();
        //关闭pop
        if (meetingMenuPop.isShowing()) {
            meetingMenuPop.dismiss();
        }
        if (meetingMeterialsPop.isShowing()) {
            meetingMeterialsPop.dismiss();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return false;
    }
}
