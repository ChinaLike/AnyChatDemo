package com.tydic.anychatmeeting.model;

import android.content.Context;
import android.view.SurfaceHolder;

import com.bairuitech.anychat.AnyChatCoreSDK;
import com.tydic.anychatmeeting.bean.SurfaceConfigBean;
import com.tydic.anychatmeeting.bean.UsersBean;
import com.tydic.anychatmeeting.constant.Key;
import com.tydic.anychatmeeting.util.SharedPreferencesUtil;
import com.tydic.anychatmeeting.util.T;
import com.tydic.anychatmeeting.widget.AnyChatView;

import java.util.List;

/**
 * 作者：like on 2018/6/7 17:15
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：视屏操作
 */
public class AnyChatControl {

    private Context context;

    private int userId;

    private AnyChatCoreSDK anychat;

    private AnyChatLayout anyChatLayout;

    public static final int USERINFO_NAME = 1;
    public static final int USERINFO_IP = 2;

    public AnyChatControl(Context context) {
        this.context = context;
        anychat = AnyChatCoreSDK.getInstance(context);
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAnyChatLayout(AnyChatLayout anyChatLayout) {
        this.anyChatLayout = anyChatLayout;
    }

    /**
     * 显示本地图像
     */
    public void showLocalVideo(SurfaceHolder surfaceHolder){
        anychat.mSensorHelper.InitSensor(context);
        AnyChatCoreSDK.mCameraHelper.SetContext(context);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        if (AnyChatCoreSDK.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_CAPDRIVER) == AnyChatDefine.VIDEOCAP_DRIVER_JAVA) {
            surfaceHolder.addCallback(AnyChatCoreSDK.mCameraHelper);
//        }

        //判断用户是否打开摄像头
//        if (SharedPreferencesUtil.getBoolean(Key.LOCAL_USER_CAMERA_KEY)){
//            openCamera(userId);
//        }else {
//            closeCamera(userId);
//        }
//        //判断用户是否打开麦克风
//        if (SharedPreferencesUtil.getBoolean(Key.LOCAL_USER_MICROPHONE_KEY)){
//            openMicrophone(userId);
//        }else {
//            closeCamera(userId);
//        }

    }

    /**
     * 初始化本地用户状态
     */
    public void initLocalStatus(){
        anychat.mSensorHelper.InitSensor(context);
        AnyChatCoreSDK.mCameraHelper.SetContext(context);
        //判断用户是否打开摄像头
//        if (SharedPreferencesUtil.getBoolean(Key.LOCAL_USER_CAMERA_KEY)){
//            anychat.UserCameraControl(userId,1);
//        }else {
//            anychat.UserCameraControl(userId,0);
//        }
//        //判断用户是否打开麦克风
//        if (SharedPreferencesUtil.getBoolean(Key.LOCAL_USER_MICROPHONE_KEY)){
//            anychat.UserSpeakControl(userId,1);
//        }else {
//            anychat.UserSpeakControl(userId,0);
//        }
    }

    /**
     * 打开摄像头
     */
    public void openCamera(int userId){
        anychat.UserCameraControl(userId,1);
        anyChatLayout.getAnyChatView(userId).openCamera();
    }

    /**
     * 关闭摄像头
     */
    public void  closeCamera(int userId){
        anychat.UserCameraControl(userId,0);
        anyChatLayout.getAnyChatView(userId).closeCamera();
    }

    /**
     * 打开麦克风
     */
    public void openMicrophone(int userId){
        anychat.UserSpeakControl(userId,1);
    }

    /**
     * 关闭麦克风
     */
    public void closeMicrophone(int userId){
        anychat.UserSpeakControl(userId,0);
    }

    /**
     * 显示远程图像
     */
    private void showRemoteVideo(SurfaceHolder surfaceHolder,int userId){
        int index = anychat.mVideoHelper.bindVideo(surfaceHolder);
        anychat.mVideoHelper.SetVideoUser(index, userId);
    }

    /**
     * 显示所有视频
     * @param list
     */
    public void showAllVideo(List<UsersBean> list){
        //先判断使用的布局是否能够完全显示在线人数，如果不能则按照布局多少显示
        int size = list.size();
        if (size > anyChatLayout.totalPosition){
            size = anyChatLayout.totalPosition;
        }
        //不管是否显示本地界面都需要采集本地信息
        initLocalStatus();
        for (int i = 0; i < size; i++) {
            UsersBean bean = list.get(i);
            int userId = bean.getUserId();
            AnyChatView anyChatView = anyChatLayout.showView(i,userId);
            if (this.userId == userId){
                //本地视频
                showLocalVideo(anyChatView.getSurfaceView().getHolder());
            }else {
                showRemoteVideo(anyChatView.getSurfaceView().getHolder(),userId);
                openCamera(userId);
                openMicrophone(userId);
            }
           anyChatView.getNickNameText().setText(bean.getNickName()+"");
        }
        anyChatLayout.setSeparateLine();
    }

    /**
     * 对本地摄像头进行开启与关闭，且在没有摄像头的情况下无法打开摄像头，在主讲人模式下只有主讲人本人可以操作自己摄像头，其他人员无法操作
     */
    public void localCamera(){
//        boolean cameraStatus = SharedPreferencesUtil.getBoolean(Key.LOCAL_USER_CAMERA_KEY);
//        int userId = SharedPreferencesUtil.getInt(Key.ANYCHAT_USER_ID);
//        if (cameraStatus){
//            //摄像头已经在打开状态
//            SharedPreferencesUtil.putBoolean(Key.LOCAL_USER_CAMERA_KEY,false);
//            closeCamera(userId);
//        }else {
//            //摄像头已经在关闭状态
//            SharedPreferencesUtil.putBoolean(Key.LOCAL_USER_CAMERA_KEY,true);
//            openCamera(userId);
//        }
    }

    /**
     * 摄像头开关
     * @param isOpen
     */
    public void camera(boolean isOpen){
//        SharedPreferencesUtil.putBoolean(Key.LOCAL_USER_CAMERA_KEY,isOpen);
//        int userId = SharedPreferencesUtil.getInt(Key.ANYCHAT_USER_ID);
//        if (isOpen){
//            openCamera(userId);
//        }else {
//            closeCamera(userId);
//        }

    }

    /**
     * 对本地麦克风进行关闭与开启，在主讲人模式下只有主讲人本人可以操作自己语音，其他人员无法操作
     */
    public void localMicrophone(){
        int microphoneStatus = SharedPreferencesUtil.getInt(Key.LOCAL_USER_MICROPHONE_KEY);
        int userId = SharedPreferencesUtil.getInt(Key.ANYCHAT_USER_ID);
        if (microphoneStatus == Key.MIC_OPEN){
            //麦克风已经在打开状态
            SharedPreferencesUtil.putInt(Key.LOCAL_USER_MICROPHONE_KEY,Key.MIC_CLOSE);
            closeMicrophone(userId);
        }else {
            //麦克风已经在关闭状态
            SharedPreferencesUtil.putInt(Key.LOCAL_USER_MICROPHONE_KEY,Key.MIC_OPEN);
            openMicrophone(userId);
        }
    }

    /**
     * 麦克风开关
     * @param isOpen
     */
    public void microphone(boolean isOpen){
//        SharedPreferencesUtil.putInt(Key.LOCAL_USER_MICROPHONE_KEY,isOpen);
//        int userId = SharedPreferencesUtil.getInt(Key.ANYCHAT_USER_ID);
//        if (isOpen){
//            openMicrophone(userId);
//        }else {
//            closeMicrophone(userId);
//        }
    }

    /**
     * 获取用户昵称
     *
     * @param userId
     * @return
     */
    public String nickName(int userId) {
        return anychat.GetUserInfo(userId, USERINFO_NAME);
    }

    /**
     * 进入房间
     * @param dwUserBean
     */
    public void enterRoom(UsersBean dwUserBean){
        int dwUserid = dwUserBean.getUserId();
        T.showShort(nickName(dwUserid) + "进入房间了！");
        SurfaceConfigBean.LayoutConfigListBean cell = anyChatLayout.getCell();
        for (SurfaceConfigBean.LayoutConfigListBean.CellInfoListBean bean:cell.getCellInfoList()){
            if (bean.getUserId() == 0){
                int position = cell.getCellInfoList().indexOf(bean);
                AnyChatView anyChatView = anyChatLayout.showView(position,dwUserid);
                showRemoteVideo(anyChatView.getSurfaceView().getHolder(),dwUserid);
                if (Key.CAMERA_OPEN ==dwUserBean.getVideoStatus() ){
                    openCamera(dwUserid);
                }else if (Key.CAMERA_CLOSE ==dwUserBean.getVideoStatus()){
                    closeCamera(dwUserid);
                }else {
                    anyChatView.noCamera();
                }
                if (Key.MIC_OPEN ==dwUserBean.getAudioStatus()){
                    openMicrophone(dwUserid);
                }else {
                    closeMicrophone(dwUserid);
                }
                return;
            }
        }
    }

    /**
     * 退出房间
     * @param dwUserid
     */
    public void exitRoom(int dwUserid){
        T.showShort(nickName(dwUserid) + "退出房间了！");
        AnyChatView anyChatView = anyChatLayout.getAnyChatView(dwUserid);
        anyChatView.exitVideo();
        SurfaceConfigBean.LayoutConfigListBean cell = anyChatLayout.getCell();
        for (SurfaceConfigBean.LayoutConfigListBean.CellInfoListBean bean:cell.getCellInfoList()){
            if (bean.getUserId() == dwUserid){
                bean.setUserId(0);
                return;
            }
        }

    }



}
