package com.tydic.anychatmeeting.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tydic.anychatmeeting.R;

/**
 * 作者：like on 2018/6/11 17:16
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：加载框
 */
public class LoadingView extends RelativeLayout {

    private LinearLayout successLayout,failLayout;

    private TextView successText;

    private Button failBtn;

    public LoadingView(Context context) {
        super(context);
        initView(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context){
        View loadingView = LayoutInflater.from(context).inflate(R.layout.item_loading,null,false);
        successLayout = loadingView.findViewById(R.id.success);
        successLayout.setVisibility(VISIBLE);
        failLayout = loadingView.findViewById(R.id.fail);
        failLayout.setVisibility(GONE);
        successText = loadingView.findViewById(R.id.loading_text);
        failBtn = loadingView.findViewById(R.id.error_btn);
        addView(loadingView);
    }

    public void loading(){
        loading("加载中...");
    }

    public void loading(String text){
        successLayout.setVisibility(VISIBLE);
        failLayout.setVisibility(GONE);
        successText.setText(text);
    }

    public void fail(){
        fail("加载失败，点击重新加载",null);
    }

    public void fail(String text){
        fail(text,null);
    }

    public void fail(OnClickListener listener){
        fail("加载失败，点击重新加载",listener);
    }

    public void fail(String text , OnClickListener listener){
        successLayout.setVisibility(GONE);
        failLayout.setVisibility(VISIBLE);
        failBtn.setText(text);
        if (listener != null) {
            failLayout.setOnClickListener(listener);
        }
    }

}
