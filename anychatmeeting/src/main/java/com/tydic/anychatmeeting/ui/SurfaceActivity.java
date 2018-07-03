package com.tydic.anychatmeeting.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

import com.tydic.anychatmeeting.R;
import com.tydic.anychatmeeting.base.BaseSurfaceActivity;
import com.tydic.anychatmeeting.bean.SurfaceConfigBean;
import com.tydic.anychatmeeting.bean.UsersBean;
import com.tydic.anychatmeeting.constant.Key;
import com.tydic.anychatmeeting.model.LayoutConfig;
import com.tydic.anychatmeeting.model.RemoteVideoControl;
import com.tydic.anychatmeeting.model.RequestData;
import com.tydic.anychatmeeting.model.SeparateLineLayout;
import com.tydic.anychatmeeting.model.inf.LayoutHelper;
import com.tydic.anychatmeeting.model.inf.OnLayoutChangeListener;
import com.tydic.anychatmeeting.ui.dialog.LayoutSettingPop;
import com.tydic.anychatmeeting.ui.dialog.OnButtonClickListener;
import com.tydic.anychatmeeting.util.ScreenUtil;
import com.tydic.anychatmeeting.util.T;

/**
 * 作者：like on 2018/5/28 14:12
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：显示直播画面
 */
public class SurfaceActivity extends BaseSurfaceActivity implements OnButtonClickListener, OnLayoutChangeListener, LayoutHelper {

    /**
     * 布局设置
     */
    private LayoutSettingPop layoutSettingPop;
    /**
     * 远程视频
     */
    private RemoteVideoControl remoteVideoControl;
    /**
     * 布局配置文件
     */
    private LayoutConfig layoutConfig;
    /**
     * 分割线
     */
    private RelativeLayout separateLineView;


    @Override
    protected int setLayout() {
        return R.layout.activity_surface;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);
        remoteVideoControl = new RemoteVideoControl(mContext, videoRoot);
        remoteVideoControl.size(ScreenUtil.getScreenWidth(mContext), ScreenUtil.getScreenHeight(mContext));
        remoteVideoControl.setLayoutHelper(this);
        layoutConfig = new LayoutConfig(mContext);
    }

    @Override
    protected void initPop() {
        super.initPop();
        //布局设置窗
        layoutSettingPop = new LayoutSettingPop(mContext, videoRoot);
        layoutSettingPop.setOnLayoutChangeListener(this);
    }


    @Override
    protected void findIds() {
        videoRoot = findViewById(R.id.video_root);
        menuLayout = findViewById(R.id.video_menu);
        menuLayout.setMenuClickListener(this);
        localAnyChatView = findViewById(R.id.localCamera);
    }

    @Override
    protected void startLayout() {
        if (remoteVideoControl != null) {
            remoteVideoControl.showAllRemoteView(onLineUserList, layoutConfig);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    /**
     * 更改布局回调
     *
     * @param bean
     */
    @Override
    public void layoutChange(SurfaceConfigBean.LayoutConfigListBean bean) {
        layoutSettingPop.dismiss();
        remoteVideoControl.switchLayout(onLineUserList, layoutConfig);
    }


    @Override
    protected void showLayoutSetting() {
        layoutSettingPop.show();
    }

    /**
     * 在线人员操作
     *
     * @param position 切换前的位置
     * @param bean
     */
    @Override
    public void positionSetting(int position, UsersBean bean) {
        onlineUserPop.dismiss();
    }

    /**
     * 位置切换操作
     *
     * @param oldPosition 切换前的位置
     * @param newPosition 切换后的位置
     */
    @Override
    public void changeLocation(int oldPosition, int newPosition) {
        onlineUserPop.dismiss();
    }

    /**
     * 主讲人设置
     *
     * @param bean
     */
    @Override
    public void speakerSetting(UsersBean bean) {
        onlineUserPop.dismiss();
    }

    /**
     * 麦克风设置
     *
     * @param bean
     */
    @Override
    public void micSetting(UsersBean bean) {
        onlineUserPop.dismiss();
    }

    /**
     * 摄像头设置
     *
     * @param bean
     */
    @Override
    public void cameraSetting(UsersBean bean) {
        onlineUserPop.dismiss();
    }

    /**
     * 进入房间
     *
     * @param enterUserBean
     */
    @Override
    protected void enterRoom(UsersBean enterUserBean) {
        remoteVideoControl.enterRoom(enterUserBean, layoutConfig);
    }

    /**
     * 退出房间
     *
     * @param exitUserid
     */
    @Override
    protected void exitRoom(int exitUserid) {
        remoteVideoControl.exitRoom(exitUserid);
    }

    @Override
    protected void remoteCamera(int userId, int cameraStatus) {
        remoteVideoControl.remoteCamera(userId, cameraStatus);
    }

    @Override
    protected void remoteMic(int userId, int micStatus) {
        remoteVideoControl.remoteMic(micStatus,userId);
    }

    @Override
    public void layout(SurfaceConfigBean.LayoutConfigListBean.CellInfoListBean bean) {
        if (bean != null) {
            localVideoControl.position((int) (ScreenUtil.getScreenWidth(mContext) * bean.getWidth() + 0.5),
                    (int) (ScreenUtil.getScreenHeight(mContext) * bean.getHeight() + 0.5),
                    (int) (ScreenUtil.getScreenHeight(mContext) * bean.getTop() + 0.5),
                    (int) (ScreenUtil.getScreenWidth(mContext) * bean.getLeft() + 0.5));
        } else {
            localVideoControl.position(1, 1, 0, 0);
        }
    }

    @Override
    public void layoutFinish() {
        setSeparateLine();
    }

    /**
     * 设置主讲人
     *
     * @param bean
     */
    @Override
    protected void setSpeaker(UsersBean bean) {
        super.setSpeaker(bean);
        //判断是否有主讲人
        if (bean.getUserId() == anyChatUserId) {
            T.showShort("您被设置为主讲人了！");
        } else {
            T.showShort(userInfoControl.nickName(bean.getUserId()) + "被设置为主讲人了！");
        }
        remoteVideoControl.speaker(bean);
    }

    /**
     * 取消主讲人
     */
    @Override
    protected void cancelSpeaker() {
        super.cancelSpeaker();
        T.showShort("已取消主讲人!");
        videoRoot.removeAllViews();
        RequestData.onLineUsers(reactBean.getRoomId(), false, this);
    }

    /**
     * 设置分割线
     */
    private void setSeparateLine() {
        SeparateLineLayout separateLineLayout = new SeparateLineLayout(layoutConfig.getCurrentLayoutConfig(), mContext);
        separateLineLayout.setSize(ScreenUtil.getScreenWidth(mContext), ScreenUtil.getScreenHeight(mContext));
        if (separateLineView != null) {
            videoRoot.removeView(separateLineView);
        }
        separateLineView = separateLineLayout.drawSeparateLine();
        videoRoot.addView(separateLineView);
    }
}
