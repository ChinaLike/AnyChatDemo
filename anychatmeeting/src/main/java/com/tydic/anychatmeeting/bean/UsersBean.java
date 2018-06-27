package com.tydic.anychatmeeting.bean;

import java.io.Serializable;

/**
 * 在线人员列表
 * Created by like on 2017-09-19
 */

public class UsersBean implements Serializable {

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    /**
     * userId : -1111
     * nickName : 阚巍_林芝
     * meetingId : 8486269380af421ab19ef03d19392f76
     * yhyUserId : 5428AA34AFF048F3803D3513636CA3A9
     * audioStatus : 1
     * videoStatus : 2
     * displayMode : 1
     * isPrimarySpeaker : 0
     */
    //复选框
    private boolean isChecked = false;

    private int userId;
    private String nickName;
    private String meetingId;
    private String yhyUserId;
    private int audioStatus;
    private int videoStatus;
    private int displayMode;

    private String isPrimarySpeaker;
    /**
     * 在界面上的位置
     */
    private int position = -1;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getYhyUserId() {
        return yhyUserId;
    }

    public void setYhyUserId(String yhyUserId) {
        this.yhyUserId = yhyUserId;
    }

    public int getAudioStatus() {
        return audioStatus;
    }

    public void setAudioStatus(int audioStatus) {
        this.audioStatus = audioStatus;
    }

    public int getVideoStatus() {
        return videoStatus;
    }

    public void setVideoStatus(int videoStatus) {
        this.videoStatus = videoStatus;
    }

    public int getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }

    public String getIsPrimarySpeaker() {
        return isPrimarySpeaker;
    }

    public void setIsPrimarySpeaker(String isPrimarySpeaker) {
        this.isPrimarySpeaker = isPrimarySpeaker;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    @Override
    public String toString() {
        return "{" +
                "isChecked=" + isChecked +
                ", userId='" + userId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", meetingId='" + meetingId + '\'' +
                ", yhyUserId='" + yhyUserId + '\'' +
                ", audioStatus='" + audioStatus + '\'' +
                ", videoStatus='" + videoStatus + '\'' +
                ", displayMode=" + displayMode +
                ", isPrimarySpeaker='" + isPrimarySpeaker + '\'' +
                ", position=" + position +
                '}';
    }
}
