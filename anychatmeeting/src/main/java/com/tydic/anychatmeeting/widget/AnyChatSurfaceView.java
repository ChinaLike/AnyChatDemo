package com.tydic.anychatmeeting.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * SurfaceView的基类文件
 */
public class AnyChatSurfaceView extends SurfaceView{
    public AnyChatSurfaceView(Context context) {
        super(context);
    }

    public AnyChatSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnyChatSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
