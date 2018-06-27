package com.tydic.anychatmeeting.ui.dialog;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.labo.kaji.relativepopupwindow.RelativePopupWindow;
import com.tydic.anychatmeeting.R;
import com.tydic.anychatmeeting.adapter.LayoutSettingAdapter;
import com.tydic.anychatmeeting.model.LayoutConfig;
import com.tydic.anychatmeeting.model.inf.OnLayoutChangeListener;
import com.tydic.anychatmeeting.util.ConvertUtil;

/**
 * 作者：like on 2018/6/6 11:04
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：更改界面布局方式
 */
public class LayoutSettingPop extends RelativePopupWindow {

    private RecyclerView recyclerView;

    private LayoutSettingAdapter adapter;

    private View rootView;

    private LayoutConfig layoutConfig;

    private OnLayoutChangeListener onLayoutChangeListener;


    public LayoutSettingPop(Context context,View rootView) {
        super(context);
        this.rootView = rootView;
        View view = LayoutInflater.from(context).inflate(R.layout.pop_layout_setting, null);
        setContentView(view);
        layoutConfig = new LayoutConfig(context);
        recyclerView = (RecyclerView) view.findViewById(R.id.page_list);
        adapter = new LayoutSettingAdapter(layoutConfig.getAllLayoutConfig(),context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        setWidth(ConvertUtil.dp2px(260));
        setHeight(ConvertUtil.dp2px(190));
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Disable default animation for circular reveal
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setAnimationStyle(0);
        }
    }

    public void setOnLayoutChangeListener(OnLayoutChangeListener onLayoutChangeListener) {
        this.onLayoutChangeListener = onLayoutChangeListener;
        adapter.setOnLayoutChangeListener(onLayoutChangeListener);
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

    /**
     * 显示弹窗
     *
     */
    public void show() {
        showOnAnchor(rootView, RelativePopupWindow.VerticalPosition.CENTER,
                RelativePopupWindow.HorizontalPosition.CENTER, false);
    }

}
