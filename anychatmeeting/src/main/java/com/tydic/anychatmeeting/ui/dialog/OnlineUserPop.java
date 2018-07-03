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
import android.view.ViewGroup;
import android.widget.TextView;

import com.labo.kaji.relativepopupwindow.RelativePopupWindow;
import com.tydic.anychatmeeting.R;
import com.tydic.anychatmeeting.adapter.OnLineUserAdapter;
import com.tydic.anychatmeeting.bean.UsersBean;
import com.tydic.anychatmeeting.constant.Key;
import com.tydic.anychatmeeting.model.RequestData;
import com.tydic.anychatmeeting.model.inf.OnItemClickListener;
import com.tydic.anychatmeeting.model.inf.OnRequestListener;
import com.tydic.anychatmeeting.react.bean.ReactBean;
import com.tydic.anychatmeeting.util.ScreenUtil;
import com.tydic.anychatmeeting.widget.LoadingView;

import java.util.ArrayList;
import java.util.List;

/**
 * 在线用户弹窗
 * Created by like on 2017-09-19
 */

public class OnlineUserPop extends RelativePopupWindow implements OnRequestListener{
    /**
     * 显示总人数
     */
    private TextView tvNum;
    /**
     * 人员列表
     */
    private RecyclerView recyclerView;
    /**
     * 加载提示框
     */
    private LoadingView loadingView;

    /**
     * 在线人员列表
     */
    private List<UsersBean> list = new ArrayList<>();

    private OnLineUserAdapter adapter;

    private View rootView;

    private Context context;

    private ReactBean reactBean;

    public void setLocationListener(OnItemClickListener locationListener) {
        adapter.setLocationListener(locationListener);
    }

    public OnlineUserPop(Context context,View rootView) {
        this.rootView = rootView;
        this.context = context;

        View view = LayoutInflater.from(context).inflate(R.layout.pop_online_list, null);
        setContentView(view);
        tvNum = view.findViewById(R.id.tvNum);
        recyclerView =  view.findViewById(R.id.recyclerView);
        loadingView = view.findViewById(R.id.loadingView);

        int width = ScreenUtil.getScreenWidth(context);
        setWidth(width * 2 / 3);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setAnimationStyle(0);
        }

    }

    /**
     * 用户数据
     * @param reactBean
     */
    public void setReactBean(ReactBean reactBean) {
        this.reactBean = reactBean;
        adapter = new OnLineUserAdapter(context, list);
        adapter.isControl(reactBean);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    /**
     * 获取在线人员列表
     */
    public void onLineUser() {
        loadingView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        loadingView.loading("在线人员获取中...");
        RequestData.onLineUsers(reactBean.getRoomId(), false, this);
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
     */
    public void show() {
        onLineUser();
        showOnAnchor(rootView, VerticalPosition.ALIGN_TOP,HorizontalPosition.RIGHT, true);
    }

    /**
     * 直接刷新界面
     */
    public void refresh(){
        if (isShowing()) {
            RequestData.onLineUsers(reactBean.getRoomId(), false, this);
        }
    }

    @Override
    public void onSuccess(int type, Object obj) {
        loadingView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        if (type == Key.ON_LINE_USER) {
            List<UsersBean> mList = (List<UsersBean>) obj;
            tvNum.setText("参会人员(" + mList.size() + ")");
            list.clear();
            list.addAll(mList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(int type, int code) {
        loadingView.fail("获取人员失败，点击重试！", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLineUser();
            }
        });
    }
}
