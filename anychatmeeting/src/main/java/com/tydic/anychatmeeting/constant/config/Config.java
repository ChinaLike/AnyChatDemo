package com.tydic.anychatmeeting.constant.config;

/**
 * 配置文件
 * Created by like on 2017-09-18
 */

public interface Config {

    //String mStrIP = "cloudmeeting.dicyun.win";//服务器地址
    //String mStrIP = "120.78.214.165";//演示环境地址
    String mStrIP = "video.xzdxcloudmeeting.cn";
    int mSPort = 8906;//端口

    String BASE_URL_ANYCHAT = "http://cloudmeeting.dicyun.win:8089/control/api/";//会控地址
    //String BASE_URL_ANYCHAT = "http://120.78.214.165:8089/control/api/";//演示环境会议地址

    String BASE_MEETING_URL = "http://cloudmeeting.dicyun.win:8088/";
    /**
     * 布局文件配置名称
     */
    String XMLConfig = "VideoConfig.json";

    /**
     * 应用视频信息配置
     */
    String APP_CLOUD_MEETING_CONFIG = "app_cloud_meeting_config";

}
