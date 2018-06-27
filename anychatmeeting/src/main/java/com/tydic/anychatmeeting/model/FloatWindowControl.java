package com.tydic.anychatmeeting.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tydic.anychatmeeting.R;
import com.tydic.anychatmeeting.bean.UsersBean;
import com.tydic.anychatmeeting.constant.Key;
import com.tydic.anychatmeeting.ui.SurfaceActivity;
import com.tydic.anychatmeeting.util.L;
import com.tydic.anychatmeeting.util.ScreenUtil;
import com.tydic.anychatmeeting.util.SharedPreferencesUtil;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;

import java.util.List;

/**
 * 作者：like on 2018/6/5 16:51
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：悬浮窗显示
 */
public class FloatWindowControl implements ViewStateListener, PermissionListener, View.OnClickListener {

    /**
     * 宽度比例
     */
    private float widthRatio = 0.3F;
    /**
     * 高度比例
     */
    private float heightRatio = 0.35F;
    /**
     * 承载视频的布局
     */
    private RelativeLayout videoRoot;

    private Context context;
    /**
     * 关闭悬浮窗
     */
    private ImageView closeFloatWindow;

    /**
     * 视频控制
     */
    private AnyChatControl anyChatControl;
    /**
     * 悬浮窗大小
     */
    private int width, height;

    private int userId;

    private List<UsersBean> onLineUserList;

    private AnyChatLayout anyChatLayout;

    public FloatWindowControl(Context context) {
        this.context = context;
        closeFloatWindow();

    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.float_window_video, null, false);
        videoRoot = view.findViewById(R.id.video_root);
        closeFloatWindow = view.findViewById(R.id.float_close);
        closeFloatWindow.setOnClickListener(this);
        width = (int) (ScreenUtil.getScreenWidth(context) * widthRatio);
        height = (int) (ScreenUtil.getScreenHeight(context) * heightRatio);
        anyChatControl = new AnyChatControl(context);
        anyChatLayout = new AnyChatLayout(context,width,height);
        userId = SharedPreferencesUtil.getInt(Key.ANYCHAT_USER_ID);
        anyChatControl.setUserId(userId);
        anyChatControl.setAnyChatLayout(anyChatLayout);
        floatWindowInit(view);
    }

    /**
     * 悬浮窗初始化
     */
    private void floatWindowInit(View view) {
        AnyChatLayout anyChatLayout = new AnyChatLayout(context, width, height);
        anyChatLayout.initLayout(videoRoot,true);
        FloatWindow
                .with(context.getApplicationContext())
                .setView(view)
                .setWidth(width) //设置悬浮控件宽高
                .setHeight(height)
                .setX(Screen.width, 0.0f)
                .setY(Screen.height, 0.3f)
                .setTag(Key.FLOAT_WINDOW_TAG)
                .setMoveType(MoveType.slide, 0, 0)
                .setMoveStyle(500, new BounceInterpolator())
                .setFilter(false, SurfaceActivity.class)
                .setViewStateListener(this)
                .setPermissionListener(this)
                .setDesktopShow(true)
                .build();
    }

    public void setOnLineUserList(List<UsersBean> onLineUserList) {
        this.onLineUserList = onLineUserList;
    }

    /**
     * 打开悬浮窗
     */
    public void openFloatWindow() {
        if (FloatWindow.get(Key.FLOAT_WINDOW_TAG) == null) {
            //不存在指定悬浮窗
            init();
            anyChatLayout.initLayout(videoRoot,true);
        } else {
            //存在指定悬浮窗切没有显示
            if (!FloatWindow.get(Key.FLOAT_WINDOW_TAG).isShowing()) {
                FloatWindow.get(Key.FLOAT_WINDOW_TAG).show();
            }
        }
        anyChatControl.showAllVideo(onLineUserList);
    }

    /**
     * 关闭悬浮窗
     */
    public void closeFloatWindow() {
        try {
            if (FloatWindow.get(Key.FLOAT_WINDOW_TAG) != null) {
                FloatWindow.destroy(Key.FLOAT_WINDOW_TAG);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFail() {

    }

    @Override
    public void onPositionUpdate(int i, int i1) {

    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

    @Override
    public void onDismiss() {

    }

    @Override
    public void onMoveAnimStart() {

    }

    @Override
    public void onMoveAnimEnd() {

    }

    @Override
    public void onBackToDesktop() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.float_close) {
            closeFloatWindow();
        }
    }
}
