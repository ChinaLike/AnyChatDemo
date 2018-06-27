package com.tydic.anychatmeeting.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.tydic.anychatmeeting.constant.Key;
import com.tydic.anychatmeeting.model.AnyChatInit;
import com.tydic.anychatmeeting.util.L;

/**
 * 作者：like on 2018/6/6 17:14
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：
 */
public class AnyChatService extends Service {

    private static final String TAG = "AnyChatService";

    private AnyChatInit anyChatInit;

    private int roomId;

    private String mStrName;

    private String mStrPwd;

    @Override
    public void onCreate() {
        super.onCreate();
        L.d("Service执行顺序onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.d("Service执行顺序onStart");
        mStrName = intent.getStringExtra(Key.SERVICE_NAME);
        mStrPwd = intent.getStringExtra(Key.SERVICE_PASSWORD);
        roomId = intent.getIntExtra(Key.SERVICE_ROOM_ID, 0);
        anyChatInit = new AnyChatInit(getApplicationContext(), roomId, mStrName, mStrPwd);
        anyChatInit.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        anyChatInit.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
