package com.tydic.anychatmeeting.model;

import android.content.Context;
import android.widget.RelativeLayout;

import com.tydic.anychatmeeting.bean.SurfaceConfigBean;
import com.tydic.anychatmeeting.widget.AnyChatView;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：like on 2018/5/28 14:28
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：处理所有布局文件，绘制分割线
 */
public class AnyChatLayout {

    private Context context;
    /**
     * 是否有分割线
     */
    private boolean devider = true;
    /**
     * 整个控件的宽度，不一定就是屏幕的宽度
     */
    private int width;
    /**
     * 整个控件的高度，不一定就是屏幕的高度
     */
    private int height;
    /**
     * 布局配置文件
     */
    private LayoutConfig layoutConfig;
    /**
     * 总行数
     */
    private int rowCount;
    /**
     * 总列数
     */
    private int columnCount;
    /**
     * 视图设置标志
     */
    private final String TAG = "AnyChat";
    /**
     * 存放视图
     */
    public Map<String, AnyChatView> anyChatViewMap = new HashMap<>();
    /**
     * 分割线
     */
    private RelativeLayout separateLineView;
    /**
     * 总共布局数量
     */
    public int totalPosition;

    private RelativeLayout rootView;

    private SurfaceConfigBean.LayoutConfigListBean cell;


    public AnyChatLayout(Context context, int width, int height) {
        this.context = context;
        this.width = width;
        this.height = height;
        layoutConfig = new LayoutConfig(context);
    }

    public void initLayout(RelativeLayout rootView, boolean devider) {
        this.rootView = rootView;
        this.devider = devider;
        cell = layoutConfig.getCurrentLayoutConfig();
        totalPosition = cell.getCellInfoList().size();
        if (cell == null) {
            throw new NullPointerException("布局文件不存在");
        }
        rowCount = cell.getRowCount();
        columnCount = cell.getColumnCount();

    }

    /**
     * 获取视图集合
     *
     * @param userId
     * @return
     */
    public AnyChatView getAnyChatView(int userId) {
        return anyChatViewMap.get(TAG + userId);
    }

    /**
     * 移除视图
     *
     * @param userId
     */
    public void removeAnyChatView(int userId) {
        if (anyChatViewMap.containsKey(TAG + userId)) {
            anyChatViewMap.remove(TAG + userId);
        }
    }

    /**
     * 设置分割线
     */
    public void setSeparateLine() {
        if (devider) {
            SeparateLineLayout separateLineLayout = new SeparateLineLayout(getCell(), context);
            separateLineLayout.setSize(width, height);
            if (separateLineView != null) {
                rootView.removeView(separateLineView);
            }
            separateLineView = separateLineLayout.drawSeparateLine();
            rootView.addView(separateLineView);
        }
    }

    /**
     * 显示视频
     *
     * @param position
     */
    public AnyChatView showView(int position, int userId) {
        SurfaceConfigBean.LayoutConfigListBean.CellInfoListBean bean = cell.getCellInfoList().get(position);
        bean.setUserId(userId);//设置该布局被那个占用
        AnyChatView anyChatView = new AnyChatView(context);
        anyChatView.size((int) (width * bean.getWidth() + 0.5), (int) (height * bean.getHeight() + 0.5));
        anyChatView.margin((int) (height * bean.getTop() + 0.5), (int) (width * bean.getLeft() + 0.5));
        anyChatView.init();
        anyChatViewMap.put(TAG + userId, anyChatView);
        rootView.addView(anyChatView);
        anyChatView.position(position);
        return anyChatView;
    }

    /**
     * 获取所有布局文件
     *
     * @return
     */
    public SurfaceConfigBean.LayoutConfigListBean getCell() {
        return layoutConfig.getCurrentLayoutConfig();
    }

    /**
     * 切换位置
     *
     * @param newPos
     */
    public void switchPosition(int newPos, int userId) {
        try {
            //移除新位置下的视图
            rootView.removeViewAt(newPos);
        } catch (Exception e) {

        }
        showView(newPos, userId);
        setSeparateLine();

    }

    /**
     * 重新设置视频的大小
     * @param userId
     * @param bean
     */
    public void resetSize(int userId , SurfaceConfigBean.LayoutConfigListBean.CellInfoListBean bean){
        AnyChatView anyChatView = anyChatViewMap.get(TAG+userId);
        if (anyChatView == null){
            anyChatView = new AnyChatView(context);
            anyChatView.size((int) (width * bean.getWidth() + 0.5), (int) (height * bean.getHeight() + 0.5));
            anyChatView.margin((int) (height * bean.getTop() + 0.5), (int) (width * bean.getLeft() + 0.5));
            anyChatView.init();
            anyChatViewMap.put(TAG + userId, anyChatView);
        }else {
            anyChatView.size((int) (width * bean.getWidth() + 0.5), (int) (height * bean.getHeight() + 0.5));
            anyChatView.margin((int) (height * bean.getTop() + 0.5), (int) (width * bean.getLeft() + 0.5));
        }

    }



}
