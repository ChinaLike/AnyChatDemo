package com.tydic.anychatmeeting.model;

import android.content.Context;
import android.media.AudioManager;

import com.bairuitech.anychat.AnyChatCoreSDK;
import com.tydic.anychatmeeting.constant.Key;
import com.tydic.anychatmeeting.model.inf.CameraHelper;
import com.tydic.anychatmeeting.model.inf.MicrophoneHelper;
import com.tydic.anychatmeeting.util.SharedPreferencesUtil;

/**
 * 作者：like on 2018/6/21 10:02
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：视频的状态控制，包括远程和本地摄像头的开关，切换，远程和本地音量的开关
 */
public class VideoStatusControl implements CameraHelper,MicrophoneHelper{

    private AnyChatCoreSDK anychat;

    private int anyChatUserId;

    private Context context;
    /**
     * 设置静音前音量
     */
    private static int volume;

    public VideoStatusControl() {
        this.anychat = AnyChatCoreSDK.getInstance(null);
    }

    public VideoStatusControl(Context context) {
        this.context = context;
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
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int volume = mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );
        if (volume != 0){
            this.volume = volume;
        }
        if (mute){
            //全部静音
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, //音量类型
                    0,
                    AudioManager.FLAG_PLAY_SOUND);
        }else {
            //静音取消
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, //音量类型
                    volume,
                    AudioManager.FLAG_PLAY_SOUND);
        }
    }
}
