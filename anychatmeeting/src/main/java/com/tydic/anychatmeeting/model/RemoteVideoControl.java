package com.tydic.anychatmeeting.model;

import android.content.Context;
import android.view.SurfaceHolder;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bairuitech.anychat.AnyChatCoreSDK;
import com.tydic.anychatmeeting.bean.SurfaceConfigBean;
import com.tydic.anychatmeeting.bean.UsersBean;
import com.tydic.anychatmeeting.constant.Key;
import com.tydic.anychatmeeting.model.inf.LayoutHelper;
import com.tydic.anychatmeeting.util.ScreenUtil;
import com.tydic.anychatmeeting.util.SharedPreferencesUtil;
import com.tydic.anychatmeeting.widget.AnyChatView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：like on 2018/6/21 12:09
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：远程视频控制
 */
public class RemoteVideoControl {

    private static final String TAG = "AnyChat";

    private Context context;

    private RelativeLayout rootView;

    private int anyChatUserId;

    private LayoutHelper layoutHelper;
    /**
     * 装在布局文件
     */
    private Map<String, AnyChatView> anyChatViewMap = new HashMap<>();
    /**
     * 视频状态控制
     */
    private VideoStatusControl videoStatusControl;
    /**
     * 显示屏幕的宽高
     */
    private int width, height;
    /**
     * 当前使用布局的大小
     */
    private int currentLayoutSize;

    public RemoteVideoControl(Context context, RelativeLayout rootView) {
        this.context = context;
        this.rootView = rootView;
        anyChatUserId = SharedPreferencesUtil.getInt(Key.ANYCHAT_USER_ID);
        videoStatusControl = new VideoStatusControl();
    }

    public void setLayoutHelper(LayoutHelper layoutHelper) {
        this.layoutHelper = layoutHelper;
    }

    /**
     * 设置布局的总大小
     *
     * @param width
     * @param height
     */
    public void size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * 显示所有的在线人数
     *
     * @param list         在线用户
     * @param layoutConfig 布局配置文件
     */
    public void showAllRemoteView(List<UsersBean> list, LayoutConfig layoutConfig) {
        anyChatViewMap.clear();
        //当前的布局文件
        SurfaceConfigBean.LayoutConfigListBean currLayout = layoutConfig.getCurrentLayoutConfig();
        if (currLayout == null) {
            return;
        }
        //每一个布局文件配置
        List<SurfaceConfigBean.LayoutConfigListBean.CellInfoListBean> layoutList = currLayout.getCellInfoList();
        currentLayoutSize = layoutList.size();
        int forSize = minNumber(list.size(), layoutList.size());
        //循环所有人,虽然有些用户不显示界面，单需要开启声音
        for (int i = 0; i < list.size(); i++) {
            UsersBean userBean = list.get(i);
            int id = userBean.getUserId();
            if (i < forSize) {
                //需要显示界面的
                if (id == anyChatUserId) {
                    layoutHelper.layout(layoutList.get(i));
                    AnyChatView anyChatView = new AnyChatView(context);
                    anyChatView.position(i);
                    anyChatViewMap.put(TAG + id, anyChatView);
                } else {
                    remoteMic(userBean.getAudioStatus(),id);
                    showRemoteView(userBean, layoutList.get(i)).position(i);
                }
            } else {
                //这些只有声音
                if (id == anyChatUserId) {
                    layoutHelper.layout(null);
                } else {
                    remoteMic(userBean.getAudioStatus(),id);
                }
            }
        }
        layoutHelper.layoutFinish();

    }

    /**
     * 语音控制
     *
     * @param micStatus
     */
    public void remoteMic(int micStatus ,int userId) {
        if (micStatus == Key.MIC_OPEN) {
            videoStatusControl.openMic(userId);
        } else if (micStatus == Key.MIC_CLOSE) {
            videoStatusControl.closeMic(userId);
        }
    }

    /**
     * 摄像头控制
     *
     * @param anyChatView
     * @param bean
     */
    private void remoteCamera(AnyChatView anyChatView, UsersBean bean) {
        if (bean.getVideoStatus() == Key.CAMERA_OPEN) {
            anyChatView.openCamera();
            videoStatusControl.openCamera(bean.getUserId());
        } else if (bean.getVideoStatus() == Key.CAMERA_CLOSE) {
            anyChatView.closeCamera();
            videoStatusControl.closeCamera(bean.getUserId());
        } else {
            anyChatView.noCamera();
            videoStatusControl.closeCamera(bean.getUserId());
        }
    }

    /**
     * 摄像头控制
     *
     * @param userId
     * @param cameraStatus
     */
    public void remoteCamera(int userId, int cameraStatus) {
        AnyChatView anyChatView = anyChatViewMap.get(TAG + userId);
        if (anyChatView != null) {
            if (cameraStatus == Key.CAMERA_OPEN) {
                anyChatView.openCamera();
                // videoStatusControl.openCamera(userId);
            } else if (cameraStatus == Key.CAMERA_CLOSE) {
                anyChatView.closeCamera();
                // videoStatusControl.closeCamera(userId);
            } else {
                anyChatView.noCamera();
                // videoStatusControl.closeCamera(userId);
            }
        }
    }

    /**
     * 两个数取较小者
     *
     * @param userSize
     * @param layoutSize
     * @return
     */
    private int minNumber(int userSize, int layoutSize) {
        int forSize = 0;
        if (userSize > layoutSize) {
            forSize = layoutSize;
        } else {
            forSize = userSize;
        }
        return forSize;
    }

    /**
     * 设置每一个人的位置
     *
     * @param bean
     */
    private AnyChatView showRemoteView(UsersBean bean, SurfaceConfigBean.LayoutConfigListBean.CellInfoListBean layoutBean) {
        AnyChatView anyChatView = new AnyChatView(context);
        anyChatView.size((int) (width * layoutBean.getWidth() + 0.5), (int) (height * layoutBean.getHeight() + 0.5));
        anyChatView.margin((int) (height * layoutBean.getTop() + 0.5), (int) (width * layoutBean.getLeft() + 0.5));
        anyChatView.init();
        setNickName(anyChatView.getNickNameText(), bean.getNickName());
        anyChatViewMap.put(TAG + bean.getUserId(), anyChatView);
        rootView.addView(anyChatView);
        showRemoteVideo(anyChatView.getSurfaceView().getHolder(), bean.getUserId());
        remoteCamera(anyChatView, bean);
        return anyChatView;
    }

    /**
     * 创建一个显示远程视频，主要用户直播模式
     * @param bean
     * @return
     */
    public AnyChatView showRemoteView(UsersBean bean){
        AnyChatView anyChatView = new AnyChatView(context);
        anyChatView.size(width,height);
        anyChatView.margin(0,0);
        anyChatView.init();
        setNickName(anyChatView.getNickNameText(), bean.getNickName());
        anyChatViewMap.put(TAG + bean.getUserId(), anyChatView);
        rootView.addView(anyChatView);
        showRemoteVideo(anyChatView.getSurfaceView().getHolder(), bean.getUserId());
        remoteCamera(anyChatView, bean);
        return anyChatView;
    }

    /**
     * 设置昵称
     *
     * @param textView
     * @param nickName
     */
    private void setNickName(TextView textView, String nickName) {
        textView.setText(nickName);
    }

    /**
     * 显示远程图像
     */
    private void showRemoteVideo(SurfaceHolder surfaceHolder, int userId) {
        int index = AnyChatCoreSDK.getInstance(null).mVideoHelper.bindVideo(surfaceHolder);
        AnyChatCoreSDK.getInstance(null).mVideoHelper.SetVideoUser(index, userId);
    }

    /**
     * 切换布局
     *
     * @param list
     * @param layoutConfig
     */
    public void switchLayout(List<UsersBean> list, LayoutConfig layoutConfig) {
        String localUserIdKey = TAG + anyChatUserId;
        //当前的布局文件
        SurfaceConfigBean.LayoutConfigListBean currLayout = layoutConfig.getCurrentLayoutConfig();
        if (currLayout == null) {
            return;
        }
        //每一个布局文件配置
        List<SurfaceConfigBean.LayoutConfigListBean.CellInfoListBean> layoutList = currLayout.getCellInfoList();
        int newLayoutSize = layoutList.size();
        if (newLayoutSize < currentLayoutSize) {
            //布局减少，移除当前界面上多余的在线人员视图
            List<String> removeKeyList = new ArrayList<>();
            for (String key : anyChatViewMap.keySet()) {
                AnyChatView anyChatView = anyChatViewMap.get(key);
                int position = anyChatView.getPosition();
                if (position < newLayoutSize) {
                    if (localUserIdKey.equals(key)) {
                        //本地视频
                        layoutHelper.layout(layoutList.get(position));
                    } else {
                        anyChatView.size((int) (width * layoutList.get(position).getWidth() + 0.5), (int) (height * layoutList.get(position).getHeight() + 0.5));
                        anyChatView.margin((int) (height * layoutList.get(position).getTop() + 0.5), (int) (width * layoutList.get(position).getLeft() + 0.5));
                    }
                } else {
                    if (localUserIdKey.equals(key)) {
                        //本地视频
                        layoutHelper.layout(null);
                    }
                    rootView.removeView(anyChatView);
                    removeKeyList.add(key);
                }

            }
            //移除多余的视图
            for (String key : removeKeyList) {
                if (anyChatViewMap.containsKey(key)) {
                    anyChatViewMap.remove(key);
                }
            }
        } else {
            List<String> alreadyAddKey = new ArrayList<>();
            //先设置已有显示重新设置布局
            for (String key : anyChatViewMap.keySet()) {
                AnyChatView anyChatView = anyChatViewMap.get(key);
                int position = anyChatView.getPosition();
                if (localUserIdKey.equals(key)) {
                    layoutHelper.layout(layoutList.get(position));
                } else {
                    anyChatView.size((int) (width * layoutList.get(position).getWidth() + 0.5), (int) (height * layoutList.get(position).getHeight() + 0.5));
                    anyChatView.margin((int) (height * layoutList.get(position).getTop() + 0.5), (int) (width * layoutList.get(position).getLeft() + 0.5));
                }
                alreadyAddKey.add(key);
            }
            if (list.size() > layoutList.size()) {
                //布局增多，会在剩下未显示的在线人员中抽取用户显示
                for (int i = anyChatViewMap.size(); i < layoutList.size(); i++) {
                    UsersBean usersBean = list.get(i);
                    if (usersBean.getUserId() == anyChatUserId) {
                        layoutHelper.layout(layoutList.get(i));
                        AnyChatView anyChatView = new AnyChatView(context);
                        anyChatView.position(i);
                        anyChatViewMap.put(TAG + usersBean.getUserId(), anyChatView);
                    } else {
                        showRemoteView(usersBean, layoutList.get(i)).position(i);
                    }
                }
            }

        }
        layoutHelper.layoutFinish();
        //最后把当前布局数修改
        currentLayoutSize = newLayoutSize;

    }

    /**
     * 进入房间，先从前到后查找是否有空位，有空位就依次布局，没有空位就在最后界面能显示的地方显示视频
     *
     * @param userBean     进入的用户
     * @param layoutConfig 布局文件
     */
    public void enterRoom(UsersBean userBean, LayoutConfig layoutConfig) {
        //当前的布局文件
        SurfaceConfigBean.LayoutConfigListBean currLayout = layoutConfig.getCurrentLayoutConfig();
        if (currLayout == null) {
            return;
        }
        //每一个布局文件配置
        List<SurfaceConfigBean.LayoutConfigListBean.CellInfoListBean> layoutList = currLayout.getCellInfoList();
        int layoutSize = layoutList.size();
        if (anyChatViewMap.size() >= layoutSize) {
            //布局已经满了，不能在进行布局
            remoteMic(userBean.getAudioStatus(),userBean.getUserId());
        } else {
            //布局未满，可进行布局
            List<Integer> positionList = getAlreadyLayoutPosition();
            for (int i = 0; i < layoutSize; i++) {
                //已占有位置不包含这个位置
                if (!positionList.contains(i)) {
                    showRemoteView(userBean, layoutList.get(i)).position(i);
                    layoutHelper.layoutFinish();
                    return;
                }
            }
        }
    }

    /**
     * 退出房间
     *
     * @param userId 退出用户ID
     */
    public void exitRoom(int userId) {
        AnyChatView anyChatView = anyChatViewMap.get(TAG + userId);
        if (anyChatView != null) {
            rootView.removeView(anyChatView);
            anyChatViewMap.remove(TAG + userId);
        }
    }

    /**
     * 获取已经占用的位置
     *
     * @return
     */
    private List<Integer> getAlreadyLayoutPosition() {
        List<Integer> list = new ArrayList<>();
        for (String key : anyChatViewMap.keySet()) {
            AnyChatView anyChatView = anyChatViewMap.get(key);
            list.add(anyChatView.getPosition());
        }
        //进行排序
        Collections.sort(list);
        return list;
    }

    /**
     * 设置主讲人
     *
     * @param bean
     */
    public void speaker(UsersBean bean) {
        rootView.removeAllViews();
        anyChatViewMap.clear();
        if (bean.getUserId() == anyChatUserId) {
            //主讲人是自己
            layoutHelper.layout(getCustomLayout());
        } else {
            layoutHelper.layout(null);
            AnyChatView anyChatView = new AnyChatView(context);
            anyChatView.size(ScreenUtil.getScreenWidth(context), ScreenUtil.getScreenHeight(context));
            anyChatView.margin(0, 0);
            anyChatView.init();
            setNickName(anyChatView.getNickNameText(), bean.getNickName());
            rootView.addView(anyChatView);
            showRemoteVideo(anyChatView.getSurfaceView().getHolder(), bean.getUserId());
            remoteCamera(anyChatView, bean);
        }
    }

    /**
     * 自定义布局
     *
     * @return
     */
    private SurfaceConfigBean.LayoutConfigListBean.CellInfoListBean getCustomLayout() {
        SurfaceConfigBean.LayoutConfigListBean.CellInfoListBean bean = new SurfaceConfigBean.LayoutConfigListBean.CellInfoListBean();
        bean.setWidth(1);
        bean.setHeight(1);
        bean.setTop(0);
        bean.setLeft(0);
        return bean;
    }

    /**
     * 切换位置
     *
     * @param anyChatView 需要切换位置的视图
     * @param bean        新位置的布局数据
     */
    public void switchPosition(AnyChatView anyChatView, SurfaceConfigBean.LayoutConfigListBean.CellInfoListBean bean) {
        anyChatView.size((int) (width * bean.getWidth() + 0.5), (int) (height * bean.getHeight() + 0.5));
        anyChatView.margin((int) (height * bean.getTop() + 0.5), (int) (width * bean.getLeft() + 0.5));
    }

}
