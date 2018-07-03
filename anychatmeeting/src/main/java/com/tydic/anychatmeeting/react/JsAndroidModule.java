package com.tydic.anychatmeeting.react;

import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.tydic.anychatmeeting.constant.Key;
import com.tydic.anychatmeeting.react.bean.ReactBean;
import com.tydic.anychatmeeting.ui.InitActivity;

/**
 * React-Native与原生通信的桥梁
 * Created by like on 2017-09-20
 */

public class JsAndroidModule extends ReactContextBaseJavaModule {

    private static final String MODULE_NAME = "JsAndroid";
    private static ReactApplicationContext mContext;

    public JsAndroidModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @ReactMethod
    public void jsActivity(String empName, String passWord, String roomId, String meetingId, String feedId,
                           String initiator, String created_by, int mode, String token, String userId) {

//        CacheUtil.get(getCurrentActivity()).put(Key.EMPNAME, empName);
//        CacheUtil.get(getCurrentActivity()).put(Key.PASSWORD, passWord);
//        CacheUtil.get(getCurrentActivity()).put(Key.USER_ID, feedId);
        ReactBean bean = new ReactBean();
        bean.setEmpName(empName);
        bean.setPassWord(passWord);
        bean.setRoomId(Integer.valueOf(roomId));
        bean.setMeetingId(meetingId);
        bean.setFeedUserName(empName);
        bean.setFeedId(feedId);
        bean.setInitiator(initiator);
        bean.setCreated_by(created_by);
        bean.setMode(mode);
        bean.setToken(token);
        bean.setUserId(userId);
        Intent intent;
        intent = new Intent(getCurrentActivity(), InitActivity.class);
        intent.putExtra(Key.REACT_PARAMS, bean);
        getCurrentActivity().startActivity(intent);
    }

    public static void sendEvent(String name) {
        mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(name, "meeting");
    }
}
