package com.tydic.anychatmeeting.ui;

import android.Manifest;
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
import com.tydic.anychatmeeting.constant.Key;
import com.tydic.anychatmeeting.constant.config.Config;
import com.tydic.anychatmeeting.model.AnyChatInit;
import com.tydic.anychatmeeting.ui.dialog.LoadingDialog;
import com.tydic.anychatmeeting.util.DelayUtil;
import com.tydic.anychatmeeting.util.SharedPreferencesUtil;
import com.tydic.anychatmeeting.util.T;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 1.摄像头和语音设置界面
 * 2.anyChat初始化
 */
public class InitActivity extends BaseActivity implements View.OnClickListener {

    private TextView microPhone;
    private TextView camera;
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
     * 用户ID
     */
    private int userId;
    /**
     * AnyChat初始类
     */
    private AnyChatInit anyChatInit;


    //摄像头状态
    private int cameraStatus;
    //麦克风状态
    private int micStatus;

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        //设置ActionBar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        loadingDialog = new LoadingDialog(mContext);
        //初始化SharedPreferencesUtil之后，以后可直接调用
        SharedPreferencesUtil.init(getApplicationContext(), Config.APP_CLOUD_MEETING_CONFIG);
        //预先初始化AnyChat
        initAnyChat();
        initImage();
        initVideoStatus();
        //预先获取权限
        permission();
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_setting;
    }

    /**
     * 初始化Anychat
     */
    private void initAnyChat() {
        anyChatInit = new AnyChatInit(mContext, reactBean.getRoomId(), reactBean.getEmpName(), reactBean.getPassWord());
        anyChatInit.start();
    }

    /**
     * 初始化视频已设置状态
     */
    private void initVideoStatus() {
        cameraStatus = SharedPreferencesUtil.getInt(Key.LOCAL_USER_CAMERA_KEY);
        if (cameraStatus == Key.CAMERA_OPEN) {
            camera.setCompoundDrawables(null, cameraDrawableOpen, null, null);
            camera.setText("摄像头已打开");
        } else if (cameraStatus == Key.CAMERA_CLOSE) {
            camera.setCompoundDrawables(null, cameraDrawableClose, null, null);
            camera.setText("摄像头已关闭");
        } else {
            camera.setText("暂不支持摄像头");
        }
        micStatus = SharedPreferencesUtil.getInt(Key.LOCAL_USER_MICROPHONE_KEY);
        if (micStatus == Key.MIC_OPEN) {
            microPhone.setCompoundDrawables(null, micDrawableOpen, null, null);
            microPhone.setText("麦克风已打开");
        } else if (micStatus == Key.MIC_CLOSE) {
            microPhone.setCompoundDrawables(null, micDrawableClose, null, null);
            microPhone.setText("麦克风已关闭");
        }
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
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        permission = true;
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        permission = false;
                        AnyChatInit.onDestroy();
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
        initBtn.setClickable(false);
        initBtn.setBackgroundResource(R.drawable.button_disclick_shape);
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
        if (cameraStatus == Key.CAMERA_OPEN) {
            //执行关闭摄像头
            camera.setCompoundDrawables(null, cameraDrawableClose, null, null);
            camera.setText("摄像头已关闭");
            SharedPreferencesUtil.putInt(Key.LOCAL_USER_CAMERA_KEY, Key.CAMERA_CLOSE);
        } else if (cameraStatus == Key.CAMERA_CLOSE) {
            //执行打开摄像头
            camera.setCompoundDrawables(null, cameraDrawableOpen, null, null);
            camera.setText("摄像头已打开");
            SharedPreferencesUtil.putInt(Key.LOCAL_USER_CAMERA_KEY, Key.CAMERA_OPEN);
        } else {
            camera.setText("暂不支持摄像头");
        }
        cameraStatus = SharedPreferencesUtil.getInt(Key.LOCAL_USER_CAMERA_KEY);
    }

    /**
     * 是否打开音频
     */
    private void isOpenSound() {
        if (micStatus == Key.MIC_OPEN) {
            //执行关闭摄像头
            microPhone.setCompoundDrawables(null, micDrawableClose, null, null);
            microPhone.setText("麦克风已关闭");
            SharedPreferencesUtil.putInt(Key.LOCAL_USER_MICROPHONE_KEY, Key.MIC_CLOSE);
        } else if (micStatus == Key.MIC_CLOSE) {
            //执行打开摄像头
            microPhone.setCompoundDrawables(null, micDrawableOpen, null, null);
            microPhone.setText("麦克风已打开");
            SharedPreferencesUtil.putInt(Key.LOCAL_USER_MICROPHONE_KEY, Key.MIC_OPEN);
        }
        micStatus = SharedPreferencesUtil.getInt(Key.LOCAL_USER_MICROPHONE_KEY);
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
            isOpenCamera();
        } else if (i == R.id.init_sound) {
            isOpenSound();
        } else if (i == R.id.init_btn) {
            feedbackState(Key.UPDATE_CLIENT_STATUS, userId);
            if ("重新连接".equals(initBtn.getText().toString())) {
                initBtn.setClickable(false);
                initBtn.setBackgroundResource(R.drawable.button_disclick_shape);
                anyChatInit.resetConnect();
                return;
            }
            loadingDialog.show();
            loadingDialog.setText("数据初始化中...");
            DelayUtil.delay(3000, new DelayUtil.DelayHelper() {
                @Override
                public void onSuccess() {
                    Intent intent;
                    if (reactBean.getMode() == Key.MODE_ONLIVE) {
                        intent = new Intent(InitActivity.this, OnLiveActivity.class);
                    } else {
                        intent = new Intent(InitActivity.this, SurfaceActivity.class);
                    }
                    intent.putExtra(Key.REACT_PARAMS, reactBean);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) {
            loadingDialog.dismiss();

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void anyChatBus(EventBusBean bean) {
        String text = "初始化中...";
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
                text = "重新连接";
                initBtn.setClickable(true);
                initBtn.setBackgroundResource(R.drawable.button_click_shape);
                break;
            case EventBusBean.ANYCHAT_START_LOGIN:
                text = "开始登录...";
                break;
            case EventBusBean.ANYCHAT_LOGIN_SUCCESS:
                text = "登录成功";
                userId = bean.dwUserId;
                SharedPreferencesUtil.putInt(Key.ANYCHAT_USER_ID, userId);
                break;
            case EventBusBean.ANYCHAT_LOGIN_FAIL:
                text = "登录失败";
                break;
            case EventBusBean.ANYCHAT_START_ENTER_ROOM:
                text = "开始进入房间";
                break;
            case EventBusBean.ANYCHAT_ENTER_ROOM_SUCCESS:
                text = "进入房间成功";
                break;
            case EventBusBean.ANYCHAT_ENTER_ROOM_FAIL:
                text = "重新连接";
                initBtn.setClickable(true);
                initBtn.setBackgroundResource(R.drawable.button_click_shape);
                break;
            case EventBusBean.ANYCHAT_ONLINE_USER_NUM:
                text = "进入房间";
                initBtn.setClickable(true);
                initBtn.setBackgroundResource(R.drawable.button_click_shape);
                break;
            case EventBusBean.ANYCHAT_USER_ENTER_ROOM:

                break;
            case EventBusBean.ANYCHAT_USER_EXIT_ROOM:
                break;
            case EventBusBean.ANYCHAT_LINK_CLOSE:
                break;

        }
        initBtn.setText(text);
    }

}
