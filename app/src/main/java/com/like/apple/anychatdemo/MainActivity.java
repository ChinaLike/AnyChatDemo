package com.like.apple.anychatdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.tydic.anychatmeeting.constant.Key;
import com.tydic.anychatmeeting.react.bean.ReactBean;
import com.tydic.anychatmeeting.ui.InitActivity;
import com.tydic.anychatmeeting.util.CacheUtil;


public class MainActivity extends AppCompatActivity {

    private EditText userName;

    private EditText pwd;

    private EditText roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = findViewById(R.id.userName);
        pwd = findViewById(R.id.pwd);
        roomId = findViewById(R.id.roomId);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InitActivity.class);
                ReactBean bean = new ReactBean();
                bean.setEmpName(userName.getText()+"");
                bean.setPassWord(pwd.getText()+"");
                bean.setRoomId(Integer.valueOf(roomId.getText()+""));
                bean.setMeetingId("6ad9f56e355e46c9b345c34ec157607b");
                bean.setFeedUserName(userName.getText()+"");
                bean.setFeedId("c254e80bae1247c499a08cb2807e447d");
                bean.setInitiator("c254e80bae1247c499a08cb2807e447d");
                bean.setCreated_by("c254e80bae1247c499a08cb2807e447d");
                bean.setIsBroadcastMode("0");
                bean.setToken("");
                bean.setUserId("");
//                CacheUtil.get(MainActivity.this).put(Key.EMPNAME, userName.getText()+"");
//                CacheUtil.get(MainActivity.this).put(Key.PASSWORD, pwd.getText()+"");
//                CacheUtil.get(MainActivity.this).put(Key.ROOM_ID, roomId.getText()+"");
//                CacheUtil.get(MainActivity.this).put(Key.USER_ID, "1656380ecc41483483f39bd5cd5de1d2");
//                CacheUtil.get(MainActivity.this).put(Key.MEETING_ID, "6ad9f56e355e46c9b345c34ec157607b");
//                CacheUtil.get(MainActivity.this).put(Key.FEED_USER_NAME, userName.getText()+"");
//                CacheUtil.get(MainActivity.this).put(Key.FEED_ID, "c254e80bae1247c499a08cb2807e447d");
//                CacheUtil.get(MainActivity.this).put(Key.INITIATOR, "c254e80bae1247c499a08cb2807e447d");
//                CacheUtil.get(MainActivity.this).put(Key.CREATED_BY, "c254e80bae1247c499a08cb2807e447d");
//                CacheUtil.get(MainActivity.this).put(Key.BROADCAST_MODE, "0");
                startActivity(intent);
            }
        });


    }
}
