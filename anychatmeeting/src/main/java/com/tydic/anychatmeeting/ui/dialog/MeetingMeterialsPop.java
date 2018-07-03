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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.labo.kaji.relativepopupwindow.RelativePopupWindow;
import com.tydic.anychatmeeting.R;
import com.tydic.anychatmeeting.adapter.MeetingMaterialsAdapter;
import com.tydic.anychatmeeting.bean.DocumentBean;
import com.tydic.anychatmeeting.constant.config.Config;
import com.tydic.anychatmeeting.model.RequestData;
import com.tydic.anychatmeeting.model.inf.OnRequestListener;
import com.tydic.anychatmeeting.overwrite.MyWebViewClient;
import com.tydic.anychatmeeting.util.ScreenUtil;
import com.tydic.anychatmeeting.widget.LoadingView;

import java.util.ArrayList;
import java.util.List;

import im.delight.android.webview.AdvancedWebView;

/**
 * 作者：like on 2018/6/13 10:45
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：
 */
public class MeetingMeterialsPop extends RelativePopupWindow implements View.OnClickListener, OnRequestListener, MeetingMaterialsAdapter.OnItemClickListener {


    private View rootView;

    private Context context;
    /**
     * 显示材料区
     */
    private AdvancedWebView advancedWebView;


    private RecyclerView materialsList;

    private ImageView back;

    private TextView title;
    /**
     * 当前界面状态：0-列表   1-详情
     */
    private int status;

    private LinearLayout parent;

    private MeetingMaterialsAdapter meetingMaterialsAdapter;

    private List<DocumentBean> mList = new ArrayList<>();

    private String userId;

    private String token;

    private LoadingView loadingView;

    private String meetingId;


    public MeetingMeterialsPop(Context context, View rootView) {
        this.rootView = rootView;
        this.context = context;

        View view = LayoutInflater.from(context).inflate(R.layout.pop_meeting_materials, null, false);
        setContentView(view);
        materialsList = (RecyclerView) view.findViewById(R.id.materialsList);
        back = (ImageView) view.findViewById(R.id.back);
        back.setOnClickListener(this);
        title = (TextView) view.findViewById(R.id.title);
        parent = (LinearLayout) view.findViewById(R.id.parent);
        loadingView = (LoadingView) view.findViewById(R.id.loadingView);

        meetingMaterialsAdapter = new MeetingMaterialsAdapter(context, mList);
        materialsList.setAdapter(meetingMaterialsAdapter);
        meetingMaterialsAdapter.setOnItemClickListener(this);
        materialsList.setLayoutManager(new LinearLayoutManager(context));

        int width = ScreenUtil.getScreenWidth(context);
        setWidth(width * 2 / 3);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(false);
        setOutsideTouchable(false);

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

    /**
     * 显示材料列表
     */
    private void showMaterialsList() {
        status = 0;
        parent.setBackgroundColor(context.getResources().getColor(R.color.bg_black));
        materialsList.setVisibility(View.VISIBLE);
        if (advancedWebView != null) {
            parent.removeView(advancedWebView);
        }
        title.setTextColor(0xFFFFFFFF);
        title.setText("材料列表");
    }

    /**
     * 显示材料详情内容
     */
    private void showMaterials(String t) {
        status = 1;
        parent.setBackgroundColor(context.getResources().getColor(R.color.white));
        title.setTextColor(0xFFCCCCCC);
        materialsList.setVisibility(View.GONE);
        advancedWebView = new AdvancedWebView(context);
        advancedWebView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        parent.addView(advancedWebView);
        title.setText(t);
    }

    public void getMaterials(String meetingId) {
        this.meetingId = meetingId;
        loadingView.loading();
        RequestData.getMeetingMaterials(meetingId, token, this);
    }

    /**
     * 显示弹窗
     */
    public void show() {
        showOnAnchor(rootView, VerticalPosition.ALIGN_TOP, HorizontalPosition.RIGHT, true);
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 打开文档
     *
     * @param url
     */
    public void openDocument(String url) {
        advancedWebView.setWebViewClient(new MyWebViewClient(context, url));
        advancedWebView.loadUrl(url);
        advancedWebView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void dismiss() {
        super.dismiss();

    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void onClick(View view) {
        if (status == 0) {
            dismiss();
        } else {
            showMaterialsList();
        }
    }

    @Override
    public void onSuccess(int type, Object obj) {
        loadingView.success();
        mList.clear();
        mList.addAll((List<DocumentBean>) obj);
        showMaterialsList();
        meetingMaterialsAdapter.notifyDataSetChanged();

    }

    @Override
    public void onError(int type, int code) {
        loadingView.fail(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingView.loading();
                getMaterials(meetingId);
            }
        });
    }

    @Override
    public void onItemClick(DocumentBean bean) {
        String url = Config.BASE_MEETING_URL + bean.getHtml_path();
        showMaterials(bean.getFile_name());
        openDocument(url);
    }
}
