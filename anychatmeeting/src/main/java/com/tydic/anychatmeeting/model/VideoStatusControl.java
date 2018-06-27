package com.tydic.anychatmeeting.model;

import com.bairuitech.anychat.AnyChatCoreSDK;
import com.tydic.anychatmeeting.model.inf.CameraHelper;
import com.tydic.anychatmeeting.model.inf.MicrophoneHelper;

/**
 * 作者：like on 2018/6/21 10:02
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：视频的状态控制，包括远程和本地摄像头的开关，切换，远程和本地音量的开关
 */
public class VideoStatusControl implements CameraHelper,MicrophoneHelper{

    private AnyChatCoreSDK anychat;

    public VideoStatusControl() {
        this.anychat = AnyChatCoreSDK.getInstance(null);
    }

    @Override
    public void openCamera(int userId) {
        anychat.UserCameraControl(userId,1);
    }

    @Override
    public void closeCamera(int userId) {
        anychat.UserCameraControl(userId,0);
    }

    @Override
    public void switchCamera() {
        AnyChatCoreSDK.mCameraHelper.SwitchCamera();
    }

    @Override
    public void openMic(int userId) {
        anychat.UserSpeakControl(userId,1);
    }

    @Override
    public void closeMic(int userId) {
        anychat.UserSpeakControl(userId,0);
    }

    @Override
    public void mute(boolean mute) {
        if (mute){
            //全部静音
        }else {
            //静音取消
        }
    }
}
