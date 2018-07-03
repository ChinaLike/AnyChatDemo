package com.tydic.anychatmeeting.ui;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tydic.anychatmeeting.R;
import com.tydic.anychatmeeting.base.BaseSurfaceActivity;
import com.tydic.anychatmeeting.bean.UsersBean;

public class OnLiveActivity extends BaseSurfaceActivity {


    @Override
    protected int setLayout() {
        return R.layout.activity_on_live;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void findIds() {

    }

    @Override
    protected void startLayout() {

    }

    /**
     * 直播模式下不需要实现
     */
    @Override
    protected void showLayoutSetting() {

    }

    @Override
    public void positionSetting(int position, UsersBean bean) {

    }

    @Override
    public void changeLocation(int oldPosition, int newPosition) {

    }

    @Override
    public void speakerSetting(UsersBean bean) {

    }

    @Override
    public void micSetting(UsersBean bean) {

    }

    @Override
    public void cameraSetting(UsersBean bean) {

    }
}
