package com.tydic.anychatmeeting.ui;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tydic.anychatmeeting.R;
import com.tydic.anychatmeeting.base.BaseSurfaceActivity;
import com.tydic.anychatmeeting.bean.UsersBean;
import com.tydic.anychatmeeting.model.RemoteVideoControl;
import com.tydic.anychatmeeting.util.ScreenUtil;
import com.tydic.anychatmeeting.widget.AnyChatView;

public class OnLiveActivity extends BaseSurfaceActivity {
    /**
     * 主讲人ID
     */
    private int speakerId;

    private RemoteVideoControl remoteVideoControl;


    @Override
    protected int setLayout() {
        return R.layout.activity_on_live;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);
        remoteVideoControl = new RemoteVideoControl(mContext,videoRoot);
    }

    @Override
    protected void findIds() {
        videoRoot = findViewById(R.id.video_root);
        menuLayout = findViewById(R.id.video_menu);
        menuLayout.setMenuClickListener(this);
        localAnyChatView = findViewById(R.id.localCamera);
    }

    /**
     * 表示没有主讲人
     */
    @Override
    protected void startLayout() {

    }

    /**
     * 有主讲人
     * @param bean
     */
    @Override
    protected void setSpeaker(UsersBean bean) {
        super.setSpeaker(bean);
        speakerId = bean.getUserId();
        if (bean.getUserId() == anyChatUserId){
            //主讲人是自己
            localVideoControl.position(ScreenUtil.getScreenWidth(mContext), ScreenUtil.getScreenHeight(mContext), 0, 0);
        }else {
            //主讲人是别人
            videoRoot.removeAllViews();
            remoteVideoControl.size(ScreenUtil.getScreenWidth(mContext), ScreenUtil.getScreenHeight(mContext));
            remoteVideoControl.showRemoteView(bean);
        }
    }

    @Override
    protected void enterRoom(UsersBean enterUserBean) {
        UsersBean speakerBean = userInfoControl.getSpeaker(onLineUserList);
        if (speakerBean != null){
            setSpeaker(speakerBean);
        }
    }

    @Override
    protected void exitRoom(int exitUserid) {
        if (exitUserid == speakerId){
            speakerId = 0;
            videoRoot.removeAllViews();
        }
    }

    @Override
    protected void remoteCamera(int userId, int cameraStatus) {
        remoteVideoControl.remoteCamera(userId, cameraStatus);
    }

    @Override
    protected void remoteMic(int userId, int micStatus) {
        remoteVideoControl.remoteMic(micStatus,userId);
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
