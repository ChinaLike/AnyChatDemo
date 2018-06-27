package com.tydic.anychatmeeting.ui.dialog;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.labo.kaji.relativepopupwindow.RelativePopupWindow;
import com.tydic.anychatmeeting.R;
import com.tydic.anychatmeeting.util.ConvertUtil;

import static com.tydic.anychatmeeting.ui.dialog.OnButtonClickListener.MENU_APPOINT_SPEAK;
import static com.tydic.anychatmeeting.ui.dialog.OnButtonClickListener.MENU_EXIT_MEETING;
import static com.tydic.anychatmeeting.ui.dialog.OnButtonClickListener.MENU_INVITATION;
import static com.tydic.anychatmeeting.ui.dialog.OnButtonClickListener.MENU_MEETING_BANNER;
import static com.tydic.anychatmeeting.ui.dialog.OnButtonClickListener.MENU_MEETING_MATERIAL;
import static com.tydic.anychatmeeting.ui.dialog.OnButtonClickListener.MENU_MEETING_PASSWORD;
import static com.tydic.anychatmeeting.ui.dialog.OnButtonClickListener.MENU_MINI_WINDOW;


/**
 * 功能菜单键
 * Created by like on 2017-09-19
 */

public class MeetingMenuPop extends RelativePopupWindow implements View.OnClickListener {

    private View rootView;

    private OnButtonClickListener menuClickListener;

    public void setMenuClickListener(OnButtonClickListener menuClickListener) {
        this.menuClickListener = menuClickListener;
    }

    public MeetingMenuPop(Context context, View rootView) {
        this.rootView = rootView;
        View view = LayoutInflater.from(context).inflate(R.layout.pop_metting_menu, null);
        setContentView(view);

        view.findViewById(R.id.llSpeak).setOnClickListener(this);
        view.findViewById(R.id.llInvite).setOnClickListener(this);
        view.findViewById(R.id.llMeetingMaterial).setOnClickListener(this);
        view.findViewById(R.id.llExit).setOnClickListener(this);
        view.findViewById(R.id.llPwd).setOnClickListener(this);
        view.findViewById(R.id.carousel).setOnClickListener(this);
        view.findViewById(R.id.llMiniWindow).setOnClickListener(this);
        setWidth(ConvertUtil.dp2px(260));
        setHeight(ConvertUtil.dp2px(190));
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setAnimationStyle(0);
        }
    }

    @Override
    public void showOnAnchor(@NonNull View anchor, int vertPos, int horizPos, int x, int y, boolean fitInScreen) {
        super.showOnAnchor(anchor, vertPos, horizPos, x, y, fitInScreen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            circularReveal(anchor);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void circularReveal(@NonNull final View anchor) {
        final View contentView = getContentView();
        contentView.post(new Runnable() {
            @Override
            public void run() {
                final int[] myLocation = new int[2];
                final int[] anchorLocation = new int[2];
                contentView.getLocationOnScreen(myLocation);
                anchor.getLocationOnScreen(anchorLocation);
                final int cx = anchorLocation[0] - myLocation[0] + anchor.getWidth() / 2;
                final int cy = anchorLocation[1] - myLocation[1] + anchor.getHeight() / 2;

                contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                final int dx = Math.max(cx, contentView.getMeasuredWidth() - cx);
                final int dy = Math.max(cy, contentView.getMeasuredHeight() - cy);
                final float finalRadius = (float) Math.hypot(dx, dy);
                Animator animator = ViewAnimationUtils.createCircularReveal(contentView, cx, cy, 0f, finalRadius);
                animator.setDuration(500);
                animator.start();
            }
        });
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (menuClickListener == null) {
            return;
        }
        if (id == R.id.llSpeak) {
            //指定发言
            menuClickListener.onVideoMenuClick(MENU_APPOINT_SPEAK);
        } else if (id == R.id.llInvite) {
            //邀请
            menuClickListener.onVideoMenuClick(MENU_INVITATION);
        } else if (id == R.id.llMeetingMaterial) {
            //会议材料
            menuClickListener.onVideoMenuClick(MENU_MEETING_MATERIAL);
        } else if (id == R.id.llPwd) {
            //会议密码
            menuClickListener.onVideoMenuClick(MENU_MEETING_PASSWORD);
        } else if (id == R.id.carousel) {
            //视频轮播
            menuClickListener.onVideoMenuClick(MENU_MEETING_BANNER);
        } else if (id == R.id.llMiniWindow) {
            //小屏小时
            menuClickListener.onVideoMenuClick(MENU_MINI_WINDOW);
        }
        if (id == R.id.llExit) {
            //退出会议
            menuClickListener.onVideoMenuClick(MENU_EXIT_MEETING);
        }
    }

    /**
     * 显示弹窗
     */
    public void show() {
        showOnAnchor(rootView, VerticalPosition.CENTER,
                HorizontalPosition.CENTER, false);
    }

}
