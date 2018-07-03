package com.tydic.anychatmeeting.overwrite;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.tydic.anychatmeeting.R;

/**
 * 作者：like on 2018/6/26 11:04
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：定制返回数据错误时候的界面
 */
public class MyWebViewClient extends WebViewClient implements View.OnClickListener{

    private Context context;

    private String url;

    private View loadingView;

    private View errorView;

    private LinearLayout errorParent;

    private boolean isLoading;

    private boolean isError;

    private WebView webView;

    public MyWebViewClient(Context context, String url) {
        this.context = context;
        this.url = url;
        init();
    }

    private void init(){
        loadingView = LayoutInflater.from(context).inflate(R.layout.layout_webview_loading,null);
        loadingView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        errorView = LayoutInflater.from(context).inflate(R.layout.layout_webview_error,null);
        errorView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        errorParent = (LinearLayout) errorView.findViewById(R.id.web_err_parent);
        errorParent.setOnClickListener(this);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        this.webView = view;
        view.addView(loadingView);
        isLoading = true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (isLoading) {
            view.removeView(loadingView);
            isLoading = false;
        }
        if (isError){
            view.removeView(errorView);
            isError = false;
        }
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        if (isLoading){
            view.removeView(loadingView);
        }
        view.addView(errorView);
        isError = true;
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        if (isLoading){
            view.removeView(loadingView);
        }
        view.addView(errorView);
        isError = true;
    }

    @Override
    public void onClick(View view) {
        webView.removeAllViews();
        webView.addView(loadingView);
        isLoading = true;
    }
}
