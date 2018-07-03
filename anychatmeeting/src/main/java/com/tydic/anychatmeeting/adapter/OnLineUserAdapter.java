package com.tydic.anychatmeeting.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tydic.anychatmeeting.R;
import com.tydic.anychatmeeting.bean.UsersBean;
import com.tydic.anychatmeeting.constant.Key;
import com.tydic.anychatmeeting.model.RequestData;
import com.tydic.anychatmeeting.model.inf.OnItemClickListener;
import com.tydic.anychatmeeting.model.inf.OnRequestListener;
import com.tydic.anychatmeeting.react.bean.ReactBean;
import com.tydic.anychatmeeting.util.T;

import java.util.List;

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

    private final int MIC_SETTING = 0x1001;
    private final int CAMERA_SETTING = 0x1002;
    private final int SPEAKER_SETTING = 0x1003;

    private ReactBean reactBean;

    public OnLineUserAdapter(Context mContext, List<UsersBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    /**
     * 获取控制权限
     */
    public void isControl(ReactBean bean) {
        this.reactBean = bean;
        String creatBy = bean.getCreated_by();
        String initiator = bean.getInitiator();
        String feedId = bean.getFeedId();
        if (feedId == null) {
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
        if (Key.MIC_OPEN == bean.getAudioStatus()) {
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
     * 主讲人功能设置
     *
     * @param tvSpeaker
     * @param bean
     * @param position
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
                    T.showShort("主讲人设置中，请稍等...");
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
            if (Key.CAMERA_OPEN == bean.getVideoStatus()) {
                tvVideo.setText("关闭视频");
            } else if (Key.CAMERA_CLOSE == bean.getVideoStatus()) {
                tvVideo.setText("打开视频");
            } else {
                tvVideo.setText("无法使用");
                return;
            }
            tvVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    T.showShort("视频设置中，请稍等...");
                    sendCameraMsg(position);
                }
            });
        }
    }

    /**
     * 麦克风按钮
     */
    private void refreshMic(final TextView tvAudio, UsersBean bean, final int position) {
        if (!canCtrl) {
            tvAudio.setVisibility(View.INVISIBLE);
            return;
        } else {
            tvAudio.setVisibility(View.VISIBLE);
            if (Key.MIC_OPEN == bean.getAudioStatus()) {
                tvAudio.setText("取消发言");
            } else {
                tvAudio.setText("允许发言");
            }
            tvAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    T.showShort("发言设置中，请稍等...");
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
                        locationListener.positionSetting(position, bean);
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
        if (type == Key.ON_LINE_USER) {
            mList.clear();
            mList.addAll((List<UsersBean>) obj);
            notifyDataSetChanged();
        } else {
            RequestData.onLineUsers(reactBean.getRoomId(), true, this);
        }
    }

    @Override
    public void onError(int type, int code) {
        if (type == SPEAKER_SETTING) {
            T.showShort("设置主讲人失败，请重新设置！");
        } else if (type == MIC_SETTING) {
            T.showShort("设置麦克风失败，请重新设置！");
        } else if (type == CAMERA_SETTING) {
            T.showShort("设置摄像头失败，请重新设置！");
        }
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
        String character;
        UsersBean dataBean = mList.get(position);
        if (Key.MIC_OPEN == dataBean.getAudioStatus()) {
            character = Key.CLIENT_DISABLE_MIC + "";
        } else {
            character = Key.CLIENT_ENABLE_MIC + "";
        }
        RequestData.sendMsg(character, dataBean, MIC_SETTING, this);
        if (locationListener != null) {
            locationListener.speakerSetting(dataBean);
        }
    }

    /**
     * 对摄像头操作
     *
     * @param position
     */
    private void sendCameraMsg(int position) {
        String character;
        UsersBean dataBean = mList.get(position);
        if (Key.CAMERA_OPEN == dataBean.getVideoStatus()) {
            character = Key.CLIENT_DISABLE_VIDEO + "";
        } else {
            character = Key.CLIENT_ENABLE_VIDEO + "";
        }
        RequestData.sendMsg(character, dataBean, CAMERA_SETTING, this);
        if (locationListener != null) {
            locationListener.speakerSetting(dataBean);
        }
    }

    /**
     * 设置主讲人
     *
     * @param position
     */
    private void sendSpeakerMsg(int position) {
        String character;
        UsersBean dataBean = mList.get(position);
        if ("1".equals(dataBean.getIsPrimarySpeaker())) {
            character = Key.CLIENT_RESET_PRIMARY_SPEAKER + "";

        } else {
            character = Key.CLIENT_SET_PRIMARY_SPEAKER + "";
        }
        RequestData.sendMsg(character, dataBean, SPEAKER_SETTING, this);
        if (locationListener != null) {
            locationListener.speakerSetting(dataBean);
        }
    }
}
