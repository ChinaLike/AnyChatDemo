package com.tydic.anychatmeeting.base;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.bairuitech.anychat.AnyChatCoreSDK;
import com.google.gson.Gson;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.tydic.anychatmeeting.constant.Key;
import com.tydic.anychatmeeting.util.CacheUtil;
import com.tydic.anychatmeeting.util.SharedPreferencesUtil;
import com.tydic.anychatmeeting.util.T;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：like on 2018/5/17 17:37
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        T.init(getApplication());
        //全屏
        initActivityStatus();
        setContentView(setLayout());
        findIds();
        EventBus.getDefault().register(this);
        init(savedInstanceState);

    }

    /**
     * 返回控件布局文件
     *
     * @return
     */
    protected abstract int setLayout();

    /**
     * 初始化，相当于Activity的onCreate()
     *
     * @param savedInstanceState
     */
    protected abstract void init(@Nullable Bundle savedInstanceState);

    protected abstract void findIds();

    /**
     * 初始化Activity的状态
     */
    protected void initActivityStatus() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕常亮
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
    }

    /**
     * 用户状态信息
     *
     * @param code
     */
    protected int feedbackState(int code, int userId) {
        int camera = SharedPreferencesUtil.getInt(Key.LOCAL_USER_CAMERA_KEY);
        int sound = SharedPreferencesUtil.getInt(Key.LOCAL_USER_MICROPHONE_KEY);
        Map<String, String> params = new HashMap<>(7);
        params.put("userId", userId + "");
        params.put("nickName", CacheUtil.get(mContext).getAsString(Key.FEED_USER_NAME));
        params.put("meetingId", CacheUtil.get(mContext).getAsString(Key.MEETING_ID));
        params.put("yhyUserId", CacheUtil.get(mContext).getAsString(Key.FEED_ID));
        params.put("audioStatus", sound + "");
        params.put("videoStatus", camera + "");
        params.put("displayMode", "1");
        String info = new Gson().toJson(params);
        return AnyChatCoreSDK.getInstance(this).UserInfoControl(userId, code, 0, 0, info);
    }
}
