package com.tydic.anychatmeeting.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bairuitech.anychat.AnyChatCoreSDK;
import com.google.gson.Gson;
import com.tydic.anychatmeeting.R;
import com.tydic.anychatmeeting.bean.UsersBean;
import com.tydic.anychatmeeting.constant.Key;
import com.tydic.anychatmeeting.model.RequestData;
import com.tydic.anychatmeeting.model.inf.OnItemClickListener;
import com.tydic.anychatmeeting.model.inf.OnRequestListener;
import com.tydic.anychatmeeting.util.CacheUtil;
import com.tydic.anychatmeeting.util.SharedPreferencesUtil;
import com.tydic.anychatmeeting.util.T;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在线用户列表
 * Created by like on 2018/1/18.
 */

public class OnLineUserAdapter extends RecyclerView.Adapter<OnLineUserAdapter.OnLineUserHolder> implements OnRequestListener {

    private Context mContext;

    private List<UsersBean> mList;

    //会议发起人或者创建者才有操作权
    private boolean canCtrl = false;

    private OnItemClickListener locationListener;

    public OnLineUserAdapter(Context mContext, List<UsersBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        isControl();
    }

    /**
     * 获取控制权限
     */
    public void isControl() {
        String creatBy = CacheUtil.get(mContext).getAsString(Key.CREATED_BY);
        String initiator = CacheUtil.get(mContext).getAsString(Key.INITIATOR);
        String feedId = CacheUtil.get(mContext).getAsString(Key.FEED_ID);
        if (feedId == null){
            return;
        }
        if (feedId.equals(creatBy) || feedId.equals(initiator)) {
            canCtrl = true;
        } else {
            canCtrl = false;
        }
    }

    /**
     * 位置监听
     *
     * @param locationListener
     */
    public void setLocationListener(OnItemClickListener locationListener) {
        this.locationListener = locationListener;
    }

    @Override
    public OnLineUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OnLineUserAdapter.OnLineUserHolder(LayoutInflater.from(mContext).inflate(R.layout.item_online, parent, false));
    }

    @Override
    public void onBindViewHolder(OnLineUserHolder holder, int position) {
        final UsersBean bean = mList.get(position);
        //麦克风图标
        refreshMicImg(holder.iv, bean);
        //昵称处理
        refreshNick(holder.tvName, bean);
        //主讲人按钮
        refreshSpeaker(holder.tvSpeaker, bean, position);
        //视频按钮
        refreshVideo(holder.tvVideo, bean, position);
        //麦克风按钮
        refreshMic(holder.tvSpeak, bean, position);
        //位置设置
        location(holder.tvLocation, bean, position);
    }

    /**
     * 对麦克风图标
     */
    private void refreshMicImg(ImageView iv, UsersBean bean) {
        if (Key.MIC_OPEN ==bean.getAudioStatus()) {
            iv.setImageResource(R.mipmap.meeting_fullscreen_microphone_enable);
        } else {
            iv.setImageResource(R.mipmap.meeting_fullscreen_microphone_disable);
        }
    }

    /**
     * 对昵称处理
     */
    private void refreshNick(TextView tvName, UsersBean bean) {
        if (Key.SPEAKER.equals(bean.getIsPrimarySpeaker())) {
            //是主讲人
            tvName.setText(bean.getNickName() + "(主讲人)");
        } else {
            tvName.setText(bean.getNickName());
        }
    }

    /**
     * 主讲人按钮
     */
    private void refreshSpeaker(TextView tvSpeaker, final UsersBean bean, final int position) {
        if (!canCtrl) {
            tvSpeaker.setVisibility(View.INVISIBLE);
            return;
        } else {
            tvSpeaker.setVisibility(View.VISIBLE);
            if (Key.SPEAKER.equals(bean.getIsPrimarySpeaker())) {
                tvSpeaker.setText("取消主讲人");
            } else {
                tvSpeaker.setText("设定主讲人");
            }
            tvSpeaker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendSpeakerMsg(position);
                }
            });
        }

    }

    /**
     * 视频按钮
     */
    private void refreshVideo(TextView tvVideo, UsersBean bean, final int position) {
        if (!canCtrl) {
            tvVideo.setVisibility(View.INVISIBLE);
            return;
        } else {
            tvVideo.setVisibility(View.VISIBLE);
            if (Key.CAMERA_OPEN ==bean.getVideoStatus()) {
                tvVideo.setText("关闭视频");
            } else if (Key.CAMERA_CLOSE ==bean.getVideoStatus()) {
                tvVideo.setText("打开视频");
            } else {
                tvVideo.setText("无法使用");
                return;
            }
            tvVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendCameraMsg(position);
                }
            });
        }
    }

    /**
     * 麦克风按钮
     */
    private void refreshMic(TextView tvAudio, UsersBean bean, final int position) {
        if (!canCtrl) {
            tvAudio.setVisibility(View.INVISIBLE);
            return;
        } else {
            tvAudio.setVisibility(View.VISIBLE);
            if (Key.MIC_OPEN ==bean.getAudioStatus()) {
                tvAudio.setText("取消发言");
            } else {
                tvAudio.setText("允许发言");
            }
            tvAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendMicMsg(position);
                }
            });
        }
    }

    /**
     * 位置设置
     */
    private void location(TextView location, final UsersBean bean, final int position) {
        boolean isSpeakerMode = false;
        for (UsersBean item : mList) {
            if (Key.SPEAKER.equals(item.getIsPrimarySpeaker())) {
                isSpeakerMode = true;
                break;
            }
        }
        if (isSpeakerMode) {
            location.setVisibility(View.GONE);
        } else {
            location.setVisibility(View.VISIBLE);
            location.setText("视频设置");
            location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (locationListener != null) {
                        locationListener.onItemClick(position, bean);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public void onSuccess(int type, Object obj) {
        if (type ==  Key.ON_LINE_USER){
            mList.clear();
            mList.addAll((List<UsersBean>) obj);
            notifyDataSetChanged();
        }else {
            RequestData.onLineUsers(Integer.parseInt(CacheUtil.get(mContext).getAsString(Key.ROOM_ID)), true, this);
        }
    }

    @Override
    public void onError(int type, int code) {
        T.showShort("信息处理失败，请重新操作！");
    }

    public class OnLineUserHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView tvName, tvSpeaker, tvVideo, tvSpeak, tvLocation;
        LinearLayout parent;

        public OnLineUserHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvSpeaker = (TextView) itemView.findViewById(R.id.tvSpeaker);
            tvVideo = (TextView) itemView.findViewById(R.id.tvVideo);
            tvSpeak = (TextView) itemView.findViewById(R.id.tvSpeak);
            tvLocation = (TextView) itemView.findViewById(R.id.tvLocation);
            parent = (LinearLayout) itemView.findViewById(R.id.parent);
        }
    }

    /**
     * 麦克风控制
     *
     * @param position
     */
    private void sendMicMsg(int position) {
        int userId;String character;
        UsersBean dataBean = mList.get(position);
        if ("1".equals(dataBean.getAudioStatus())) {
            character = Key.CLIENT_DISABLE_MIC + "";
        } else {
            character = Key.CLIENT_ENABLE_MIC + "";
        }
        userId = mList.get(position).getUserId();
        RequestData.sendMsg(userId, character, dataBean, 0x10001, this);
    }

    /**
     * 对摄像头操作
     *
     * @param position
     */
    private void sendCameraMsg(int position) {
        int userId;String character;
        UsersBean dataBean = mList.get(position);
        if ("2".equals(dataBean.getVideoStatus())) {
            character = Key.CLIENT_DISABLE_VIDEO + "";

        } else {
            character = Key.CLIENT_ENABLE_VIDEO + "";
        }
        userId = mList.get(position).getUserId();
        RequestData.sendMsg(userId, character, dataBean, 0x10002, this);
    }

    /**
     * 设置主讲人
     *
     * @param position
     */
    private void sendSpeakerMsg(int position) {
        int userId;String character;
        UsersBean dataBean = mList.get(position);
        if ("1".equals(dataBean.getIsPrimarySpeaker())) {
            character = Key.CLIENT_RESET_PRIMARY_SPEAKER + "";

        } else {
            character = Key.CLIENT_SET_PRIMARY_SPEAKER + "";
        }
        userId = mList.get(position).getUserId();
        RequestData.sendMsg(userId, character, dataBean, 0x1003, this);
    }

    /**
     * 用户状态信息
     *
     * @param code
     */
    protected int feedbackState(int code,UsersBean bean,String audioStatus , String videoStatus) {
        boolean camera = SharedPreferencesUtil.getBoolean(Key.LOCAL_USER_CAMERA_KEY);
        boolean sound = SharedPreferencesUtil.getBoolean(Key.LOCAL_USER_MICROPHONE_KEY);
        int userId = bean.getUserId();
        Map<String, String> params = new HashMap<>(7);
        params.put("userId", userId + "");
        params.put("nickName", bean.getNickName());
        params.put("meetingId",bean.getMeetingId());
        params.put("yhyUserId", bean.getYhyUserId());
        params.put("audioStatus", audioStatus);
        params.put("videoStatus", videoStatus);
        params.put("displayMode", "1");
        String info = new Gson().toJson(params);
        return AnyChatCoreSDK.getInstance(mContext).UserInfoControl(userId, code, 0, 0, info);
    }
}
