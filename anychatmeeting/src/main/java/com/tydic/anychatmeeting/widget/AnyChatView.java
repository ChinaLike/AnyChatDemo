package com.tydic.anychatmeeting.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tydic.anychatmeeting.R;
import com.tydic.anychatmeeting.helper.VideoHelper;
import com.tydic.anychatmeeting.helper.ViewHelper;

/**
 * 作者：like on 2018/5/17 17:46
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：主要用来显示界面视频，界面所有功能全部交给控件处理，每一个视频显示相互不影响
 */

public class AnyChatView extends RelativeLayout implements ViewHelper,VideoHelper {

    /**
     * 根布局
     */
    private View rootView;
    /**
     * 摄像头状态
     */
    private ImageView cameraStatus;
    /**
     * 显示视频的控件
     */
    private AnyChatSurfaceView surfaceView;
    /**
     * 昵称
     */
    private TextView nickName;

    private RelativeLayout cameraParent;
    /**
     * 视图大小
     */
    private int width, height;
    /**
     * 视频显示对应的下标
     */
    private int position;

    private Context context;

    public AnyChatView(Context context) {
        super(context);
        this.context = context;
        creat(context);
    }

    public AnyChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        creat(context);
    }

    public AnyChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        creat(context);
    }

    /**
     * 创建布局文件
     *
     * @param context
     */
    private void creat(Context context) {
        rootView = LayoutInflater.from(context).inflate(R.layout.layout_anychat, null, false);
        surfaceView = rootView.findViewById(R.id.surface_view);
        cameraStatus = rootView.findViewById(R.id.camera_status);
        nickName = rootView.findViewById(R.id.nickname);
        cameraParent = rootView.findViewById(R.id.camera_parent);
    }

    /**
     * 界面初始化
     */
    public void init() {
        addView(rootView);
    }


    @Override
    public void size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void margin(int topMargin, int leftMargin) {
        //设置SurfaceView父类的大小
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.topMargin = topMargin;
        layoutParams.leftMargin = leftMargin;
        setLayoutParams(layoutParams);
    }

    /**
     * 视频显示在界面对应的下标
     * @param position
     */
    @Override
    public void position(int position) {
        this.position = position;
    }

    /**
     * 获取视频对应的下标
     * @return
     */
    public int getPosition() {
        return position;
    }

    public ImageView getCameraStatusImageView() {
        return cameraStatus;
    }

    public AnyChatSurfaceView getSurfaceView() {
        return surfaceView;
    }

    public TextView getNickNameText() {
        return nickName;
    }

    @Override
    public void openCamera() {
        surfaceView.setVisibility(VISIBLE);
        cameraParent.setVisibility(GONE);
        surfaceView.setZOrderOnTop(false);
        surfaceView.setZOrderMediaOverlay(true);
        surfaceView.setBackgroundColor(Color.TRANSPARENT);

    }

    @Override
    public void closeCamera() {
        surfaceView.setVisibility(GONE);
        cameraParent.setVisibility(VISIBLE);
        //设置背景颜色
        cameraParent.setBackgroundColor(context.getResources().getColor(R.color.hava_carame));
        //设置关闭摄像头图片
        cameraStatus.setBackgroundResource(R.drawable.shut_camera);

    }

    @Override
    public void noCamera() {
        surfaceView.setVisibility(GONE);
        cameraParent.setVisibility(VISIBLE);
        //设置背景颜色
        cameraParent.setBackgroundColor(context.getResources().getColor(R.color.hava_carame));
        //设置关闭摄像头图片
        cameraStatus.setBackgroundResource(R.drawable.no_camera);
    }

    @Override
    public void exitVideo() {
        removeAllViews();
    }
}
