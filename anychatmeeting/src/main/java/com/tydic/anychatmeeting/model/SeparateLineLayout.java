package com.tydic.anychatmeeting.model;

import android.content.Context;
import android.widget.RelativeLayout;

import com.tydic.anychatmeeting.bean.SurfaceConfigBean;
import com.tydic.anychatmeeting.widget.SeparateLineView;

/**
 * 作者：like on 2018/6/5 11:17
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：分割线整体布局
 */
public class SeparateLineLayout {

    /**
     * 选择的布局
     */
    private SurfaceConfigBean.LayoutConfigListBean bean;
    /**
     * 总行数
     */
    private int rowCount;
    /**
     * 总列数
     */
    private int columnCount;

    private Context context;

    private RelativeLayout deviderLayout;

    /**
     * 分割线宽度
     */
    private int deviderWidth = 2;
    /**
     * 布局大小
     */
    private int width, height;

    public SeparateLineLayout(SurfaceConfigBean.LayoutConfigListBean bean, Context context) {
        this.bean = bean;
        this.context = context;
        rowCount = bean.getRowCount();
        columnCount = bean.getColumnCount();
        deviderLayout = new RelativeLayout(context);
    }

    /**
     * 设置大小
     * @param width
     * @param height
     */
    public void setSize(int width,int height){
        this.width = width;
        this.height =height;
    }

    /**
     * 绘制分割线
     *
     * @return
     */
    public RelativeLayout drawSeparateLine() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        deviderLayout.setLayoutParams(layoutParams);
        for (SurfaceConfigBean.LayoutConfigListBean.CellInfoListBean item : bean.getCellInfoList()) {
            int row = item.getRowIndex();
            int col = item.getColumnIndex();
            addHorizontalDevider(row,item);
            addVerticalDevider(col,item);
        }

        return deviderLayout;
    }

    /**
     * 添加水平分割线
     *
     * @param row 行数
     */
    private void addHorizontalDevider(int row, SurfaceConfigBean.LayoutConfigListBean.CellInfoListBean item) {
        int left = (int) (width*item.getLeft() +0.5);
        int top = (int) (height*item.getTop() + height*item.getHeight()+0.5);
        if (rowCount <= 1) {
            return;
        }
        SeparateLineView bottomView = new SeparateLineView(context, RelativeLayout.LayoutParams.MATCH_PARENT, deviderWidth, top, left);
        if (row != rowCount - 1) {
            deviderLayout.addView(bottomView);
        }
    }


    /**
     * 添加垂直分割线
     *
     * @param col 列数
     */
    private void addVerticalDevider(int col, SurfaceConfigBean.LayoutConfigListBean.CellInfoListBean item) {
        int left = (int) (width*item.getLeft() + width*item.getWidth()+0.5);
        int top = (int) (height*item.getTop() +0.5);
        if (columnCount <= 1) {
            return;
        }
        SeparateLineView rightView = new SeparateLineView(context, deviderWidth, RelativeLayout.LayoutParams.MATCH_PARENT, top, left);
        if (col != columnCount - 1) {
            deviderLayout.addView(rightView);
        }
    }
}
