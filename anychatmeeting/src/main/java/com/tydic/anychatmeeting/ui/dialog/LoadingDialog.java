package com.tydic.anychatmeeting.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.tydic.anychatmeeting.R;

/**
 * 作者：like on 2018/6/7 11:53
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：进度弹窗
 */
public class LoadingDialog extends Dialog{

    private TextView mTextView;
    private View mImageView;

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.Theme_Light_CustomDialog_Blue);
        setCancelable(true);
        setContentView(R.layout.dialog_loading);
        mTextView = (TextView) findViewById(R.id.textview);
        mImageView = findViewById(R.id.imageview);
    }

    /**
     * 设置对话框显示文本
     *
     * @param text
     */
    public final void setText(CharSequence text) {
        mTextView.setText(text);
    }

    public final void dismiss() {
        super.dismiss();
        mImageView.clearAnimation();
    }

    public final void show() {
        super.show();
        Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.loading);
        mImageView.startAnimation(loadAnimation);
    }

}
