package com.tydic.anychatmeeting.react.bean;

import com.tydic.anychatmeeting.constant.Key;

import java.io.Serializable;

/**
 * 由React-native传递过来的参数
 * Created by like on 2017-09-18
 */

public class ReactBean implements Serializable {

    private String empName;
    private String passWord;
    private Integer roomId;//房间的ID
    private String meetingId;//会议的ID
    private String feedUserName;//主讲人的名字
    private String feedId;//主讲人的ID
    private String initiator;//发起人
    private String created_by;//创建者
    private int  mode;//是否是直播模式
    private String token;
    private String userId;




    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getFeedUserName() {
        return feedUserName;
    }

    public void setFeedUserName(String feedUserName) {
        this.feedUserName = feedUserName;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }



    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "{" +
                "empName='" + empName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", roomId=" + roomId +
                ", meetingId='" + meetingId + '\'' +
                ", feedUserName='" + feedUserName + '\'' +
                ", feedId='" + feedId + '\'' +
                ", initiator='" + initiator + '\'' +
                ", created_by='" + created_by + '\'' +
                ", mode='" + mode + '\'' +
                ", token='" + token + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
