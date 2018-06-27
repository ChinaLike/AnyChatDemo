package com.tydic.anychatmeeting.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.tydic.anychatmeeting.R;
import com.tydic.anychatmeeting.base.BaseActivity;
import com.tydic.anychatmeeting.bean.EventBusBean;
import com.tydic.anychatmeeting.bean.UsersBean;
import com.tydic.anychatmeeting.constant.Key;
import com.tydic.anychatmeeting.constant.config.Config;
import com.tydic.anychatmeeting.model.RequestData;
import com.tydic.anychatmeeting.model.inf.OnRequestListener;
import com.tydic.anychatmeeting.react.bean.ReactBean;
import com.tydic.anychatmeeting.service.AnyChatService;
import com.tydic.anychatmeeting.ui.dialog.LoadingDialog;
import com.tydic.anychatmeeting.util.CacheUtil;
import com.tydic.anychatmeeting.util.ClassUtil;
import com.tydic.anychatmeeting.util.DelayUtil;
import com.tydic.anychatmeeting.util.L;
import com.tydic.anychatmeeting.util.SharedPreferencesUtil;
import com.tydic.anychatmeeting.util.T;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 1.摄像头和语音设置界面
 * 2.anyChat初始化
 */
public class InitActivity extends BaseActivity implements View.OnClickListener, OnRequestListener {

    private TextView microPhone;
    private TextView camera;
    private boolean isOpenCamera = true;
    private boolean isOpenSound = true;
    private Button initBtn;

    private Drawable cameraDrawableClose;
    private Drawable cameraDrawableOpen;

    private Drawable micDrawableClose;
    private Drawable micDrawableOpen;

    private boolean permission = false;

    /**
     * 显示加载框
     */
    private LoadingDialog loadingDialog;
    /**
     * 房间ID
     */
    private int roomId;
    /**
     * 用户ID
     */
    private int userId;

    private static Intent intent;

    private static Context instance;

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        instance = mContext;




        //进度框
        loadingDialog = new LoadingDialog(this);
        //获取房间号
        roomId = Integer.parseInt(CacheUtil.get(this).getAsString(Key.ROOM_ID));
        SharedPreferencesUtil.init(getApplicationContext(), Config.APP_CLOUD_MEETING_CONFIG);
        //设置ActionBar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initData();
        initImage();
        isOpenCamera();
        isOpenSound();

        permission();
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_setting;
    }

    private void getReactBean(){
        Intent intent = getIntent();
        ReactBean reactBean = (ReactBean) intent.getExtras().getSerializable(Key.REACT_PARAMS);
    }

    /**
     * 权限获取
     */
    private void permission() {
        Acp.getInstance(this).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.CAMERA
                                , Manifest.permission.RECORD_AUDIO
                                , Manifest.permission.READ_PHONE_STATE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        /*以下为自定义提示语、按钮文字
                        .setDeniedMessage()
                        .setDeniedCloseBtn()
                        .setDeniedSettingBtn()
                        .setRationalMessage()
                        .setRationalBtn()*/
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        permission = true;
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        permission = false;
                        T.showShort("未授予相关权限，将退出！");
                        finish();
                    }
                });
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

    }


    /**
     * 获取控件实例
     */
    @Override
    protected void findIds() {
        microPhone = (TextView) findViewById(R.id.init_sound);
        microPhone.setOnClickListener(this);
        camera = (TextView) findViewById(R.id.init_camera);
        camera.setOnClickListener(this);
        initBtn = (Button) findViewById(R.id.init_btn);
        initBtn.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        int cameraStatus = SharedPreferencesUtil.getInt(Key.LOCAL_USER_CAMERA_KEY);
        isOpenCamera = cameraStatus == Key.CAMERA_OPEN ? true : false;
        int micStatus = SharedPreferencesUtil.getInt(Key.LOCAL_USER_MICROPHONE_KEY);
        isOpenSound = micStatus == Key.MIC_OPEN ? true : false;
    }

    /**
     * 初始化图片参数
     */
    private void initImage() {
        cameraDrawableClose = getResources().getDrawable(R.mipmap.img_meeting_camera_close);
        cameraDrawableOpen = getResources().getDrawable(R.mipmap.img_meeting_camera_open);
        micDrawableClose = getResources().getDrawable(R.mipmap.meeting_microphone_disable);
        micDrawableOpen = getResources().getDrawable(R.mipmap.img_meeting_microphone);
        int width = cameraDrawableClose.getIntrinsicWidth();
        int height = cameraDrawableClose.getIntrinsicHeight();
        cameraDrawableClose.setBounds(0, 0, width, height);
        cameraDrawableOpen.setBounds(0, 0, width, height);
        micDrawableClose.setBounds(0, 0, width, height);
        micDrawableOpen.setBounds(0, 0, width, height);
    }

    /**
     * 是否打开视频
     */
    private void isOpenCamera() {
        if (!isOpenCamera) {
            //执行关闭摄像头
            camera.setCompoundDrawables(null, cameraDrawableClose
                    , null, null);
            camera.setText("摄像头已关闭");
        } else {
            //执行打开摄像头
            camera.setCompoundDrawables(null, cameraDrawableOpen
                    , null, null);
            camera.setText("摄像头已打开");
        }
    }

    /**
     * 是否打开音频
     */
    private void isOpenSound() {


        if (!isOpenSound) {
            //执行关闭摄像头
            microPhone.setCompoundDrawables(null, micDrawableClose
                    , null, null);
            microPhone.setText("麦克风已关闭");
        } else {
            //执行打开摄像头
            microPhone.setCompoundDrawables(null, micDrawableOpen
                    , null, null);
            microPhone.setText("麦克风已打开");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.init_camera) {
            isOpenCamera = !isOpenCamera;
            isOpenCamera();
            SharedPreferencesUtil.putInt(Key.LOCAL_USER_CAMERA_KEY, isOpenCamera ? Key.CAMERA_OPEN : Key.CAMERA_CLOSE);
        } else if (i == R.id.init_sound) {
            isOpenSound = !isOpenSound;
            isOpenSound();
            SharedPreferencesUtil.putInt(Key.LOCAL_USER_MICROPHONE_KEY, isOpenSound ? Key.MIC_OPEN : Key.MIC_CLOSE);
        } else if (i == R.id.init_btn) {
            if (permission) {
                requestPermissionsSuccess();
            }
        }
    }

    /**
     * 关闭Service
     */
    public static void stopService() {
        instance.stopService(intent);
        intent = null;
        instance = null;
    }

    /**
     * 初始化Anychat
     */
    private void initAnyChat() {
        intent = new Intent(InitActivity.this, AnyChatService.class);
        intent.putExtra(Key.SERVICE_ROOM_ID, roomId);
        intent.putExtra(Key.SERVICE_NAME, CacheUtil.get(this).getAsString(Key.EMPNAME));
        intent.putExtra(Key.SERVICE_PASSWORD, CacheUtil.get(this).getAsString(Key.PASSWORD));
        startService(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void anyChatBus(EventBusBean bean) {
        String text = "";
        switch (bean.type) {
            case EventBusBean.ANYCHAT_INIT:
                text = "初始化中...";
                break;
            case EventBusBean.ANYCHAT_START_CONNECT:
                text = "开始连接...";
                break;
            case EventBusBean.ANYCHAT_CONNECT_SUCCESS:
                text = "连接成功";
                break;
            case EventBusBean.ANYCHAT_CONNECT_FAIL:
                text = "连接失败";
                break;
            case EventBusBean.ANYCHAT_START_LOGIN:
                text = "开始登录...";
                break;
            case EventBusBean.ANYCHAT_LOGIN_SUCCESS:
                text = "登录成功";
                userId = bean.dwUserId;
                SharedPreferencesUtil.putInt(Key.ANYCHAT_USER_ID, userId);
                feedbackState(Key.UPDATE_CLIENT_STATUS, userId);
                break;
            case EventBusBean.ANYCHAT_LOGIN_FAIL:
                text = "登录失败";
                break;
            case EventBusBean.ANYCHAT_START_ENTER_ROOM:
                text = "开始进入房间";
                break;
            case EventBusBean.ANYCHAT_ENTER_ROOM_SUCCESS:
                text = "进入房间成功";
                DelayUtil.delay(2000, new DelayUtil.DelayHelper() {
                    @Override
                    public void onSuccess() {
                        RequestData.onLineUsers(roomId, false, InitActivity.this);
                    }
                });
                break;
            case EventBusBean.ANYCHAT_ENTER_ROOM_FAIL:
                text = "进入房间失败";
                break;
            case EventBusBean.ANYCHAT_ONLINE_USER_NUM:
                text = "获取房间在线人数成功";

                break;
            case EventBusBean.ANYCHAT_USER_ENTER_ROOM:

                break;
            case EventBusBean.ANYCHAT_USER_EXIT_ROOM:
                break;
            case EventBusBean.ANYCHAT_LINK_CLOSE:
                break;

        }
        loadingDialog.setText(text);
    }

    @Override
    public void onSuccess(int type, Object obj) {
        loadingDialog.setText("获取在线人数成功");
        loadingDialog.dismiss();
        List<UsersBean> list = (ArrayList<UsersBean>) obj;
        L.d("SettingActivity在线人数", list.toString());

        Intent intent = new Intent(InitActivity.this, SurfaceActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("userList", (Serializable) list);
        startActivity(intent);
        finish();
    }

    @Override
    public void onError(int type, int code) {

    }


    public void requestPermissionsSuccess() {
        loadingDialog.show();
        if (ClassUtil.isServiceRunning(mContext, "com.tydic.anychatmeeting.service.AnyChatService")) {
            loadingDialog.setText("正在获取在线人数...");
            userId = SharedPreferencesUtil.getInt(Key.ANYCHAT_USER_ID);
            feedbackState(Key.UPDATE_CLIENT_STATUS, userId);
            DelayUtil.delay(2000, new DelayUtil.DelayHelper() {
                @Override
                public void onSuccess() {
                    RequestData.onLineUsers(roomId, false, InitActivity.this);
                }
            });
        } else {
            initAnyChat();
        }
    }
}
