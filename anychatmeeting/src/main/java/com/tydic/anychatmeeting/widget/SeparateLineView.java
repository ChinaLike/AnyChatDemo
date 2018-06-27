package com.tydic.anychatmeeting.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 作者：like on 2018/6/1 13:28
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：分割线
 */
public class SeparateLineView extends View {

    private int deviderColor = Color.WHITE;

    public SeparateLineView(Context context) {
        super(context);
    }

    public SeparateLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SeparateLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SeparateLineView(Context context, int width, int height, int topMargin, int leftMargin) {
        super(context);
        init(width, height, topMargin, leftMargin);
    }

    private void init(int width, int height, int topMargin, int leftMargin) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        params.topMargin = topMargin;
        params.leftMargin = leftMargin;
        setLayoutParams(params);
        setBackgroundColor(deviderColor);
    }
}
