package com.tydic.anychatmeeting.model;

import android.content.Context;
import android.view.SurfaceHolder;

import com.bairuitech.anychat.AnyChatCoreSDK;
import com.bairuitech.anychat.AnyChatDefine;
import com.tydic.anychatmeeting.constant.Key;
import com.tydic.anychatmeeting.widget.AnyChatView;

/**
 * 作者：like on 2018/6/21 10:01
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：针对本地视频控制
 */
public class LocalVideoControl {

    private Context context;
    /**
     * 视频控件
     */
    private AnyChatView anyChatView;
    /**
     * 视频状态控制
     */
    public VideoStatusControl videoStatusControl;

    public LocalVideoControl(Context context, AnyChatView anyChatView) {
        this.context = context;
        this.anyChatView = anyChatView;
        videoStatusControl = new VideoStatusControl(context);
        initAnyChat(anyChatView.getSurfaceView().getHolder());
    }

    private void initAnyChat(SurfaceHolder surfaceHolder) {
        AnyChatCoreSDK.getInstance(null).mSensorHelper.InitSensor(context);
        AnyChatCoreSDK.mCameraHelper.SetContext(context);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        if (AnyChatCoreSDK.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_CAPDRIVER) == AnyChatDefine.VIDEOCAP_DRIVER_JAVA) {
            surfaceHolder.addCallback(AnyChatCoreSDK.mCameraHelper);
        }
    }

    /**
     * 本地摄像头控制
     */
    public void camera(int cameraStatus) {
        if (cameraStatus == Key.CAMERA_OPEN) {
            //摄像头开启
            anyChatView.openCamera();
            videoStatusControl.openCamera(-1);
        } else if (cameraStatus == Key.CAMERA_CLOSE) {
            //摄像头关闭
            anyChatView.closeCamera();
            videoStatusControl.closeCamera(-1);
        } else {
            //没有摄像头
            anyChatView.noCamera();
        }
    }

    /**
     * 麦克风状态
     *
     * @param micStatus
     */
    public void microphone(int micStatus) {
        if (micStatus == Key.MIC_OPEN) {
            //麦克风开启
            videoStatusControl.openMic(-1);
        } else {
            //麦克风关闭
            videoStatusControl.closeMic(-1);
        }
    }

    /**
     * 设置视频显示位置
     *
     * @param width
     * @param height
     * @param topMargin
     * @param leftMargin
     */
    public void position(int width, int height, int topMargin, int leftMargin) {
        anyChatView.size(width, height);
        anyChatView.margin(topMargin, leftMargin);
    }

    /**
     * 媒体音量控制
     * @param mute
     */
    public void mute(boolean mute){
        videoStatusControl.mute(mute);
    }


}
