package com.tydic.anychatmeeting.constant;

/**
 * 作者：like on 2018/5/31 10:52
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：
 */
public interface Key {
    /**
     * React-Native传递过来的值的Key
     */
    String REACT_PARAMS = "react_params";
    /**
     * 以下为存储到SharedPreferences中的Key
     */
    String SHAREDPREFERENCES_EMPNAME = "SharedPreferences_empName";
    String SHAREDPREFERENCES_PASSWORD = "SharedPreferences_passWord";
    String SHAREDPREFERENCES_ROOM_ID = "SharedPreferences_roomId";
    String SHAREDPREFERENCES_MEETING_ID = "SharedPreferences_meetingId";
    String SHAREDPREFERENCES_FEED_USER_NAME = "SharedPreferences_feedUserName";
    String SHAREDPREFERENCES_FEED_ID = "SharedPreferences_feedId";
    String SHAREDPREFERENCES_INITIATOR = "SharedPreferences_initiator";
    String SHAREDPREFERENCES_CREATED_BY = "SharedPreferences_created_by";
    String SHAREDPREFERENCES_IS_BROADCAST_MODE = "SharedPreferences_isBroadcastMode";
    String SHAREDPREFERENCES_TOKEN = "SharedPreferences_token";
    String SHAREDPREFERENCES_USER_ID = "SharedPreferences_userId";




    int SUCCESS = 0;//成功

    int FAIL = 1;//失败

    int OVER_TIME = -1;//超时

    int ON_LINE_USER = 0;//在线人员

    int SEND_MESSAGE = 1;//发送消息

    int USER_STATE = 2;//用户状态

    int MEETING_MATERIALS = 3;//会议材料


    int UPDATE_CLIENT_STATUS = 1001;
    int CLIENT_DISABLE_MIC = 1002;
    int CLIENT_ENABLE_MIC = 1003;
    int CLIENT_DISABLE_VIDEO = 1004;
    int CLIENT_ENABLE_VIDEO = 1005;
    int CLIENT_SET_PRIMARY_SPEAKER = 1006;
    int CLIENT_NOTICE_PRIMARY_SPEAKER = 1007;
    int CLIENT_RESET_PRIMARY_SPEAKER = 1008;

    String EMPNAME = "EMPNAME";
    //登录用户
    String USER_ID = "USER_ID";
    //登录密码
    String PASSWORD = "PASSWORD";
    //房间ID
    String ROOM_ID = "ROOM_ID";
    //会议ID
    String MEETING_ID = "MEETING_ID";
    //用户昵称
    String FEED_USER_NAME = "FEED_USER_NAME";
    //
    String FEED_ID = "FEED_ID";
    //
    String INITIATOR = "INITIATOR";
    //创建者
    String CREATED_BY = "CREATED_BY";
    //直播模式
    String BROADCAST_MODE = "BROADCAST_MODE";
    //AnyChat的UserId
    String ANYCHAT_USER_ID = "ANYCHAT_USER_ID";

    int CAMERA_OTHER = -1;//人员已经退出，恢复摄像头初始状态
    int CAMERA_NO = 0;//没有摄像头
    int CAMERA_CLOSE = 1;//有摄像头但是是关闭状态
    int CAMERA_OPEN = 2;//摄像头打开状态

    int MIC_CLOSE = 0;//麦克风关闭
    int MIC_OPEN = 1;//麦克风打开

    int SOUND_CLOSE = 0;//扩音关闭
    int SOUND_OPEN = 1;//扩音打开

    int MODE_ONLIVE = 1;//直播模式
    int MODE_COMMON = 0;//普通模式

    String SPEAKER = "1";//是主讲人
    String NO_SPEAKER = "0";//不是主讲人
    String IS_START = "IS_START";
    //悬浮窗标志
    String FLOAT_WINDOW_TAG = "float-window";

    /**
     * 配置文件默认使用ID的Key
     */
    String LAYOUT_CONFIG_DEFAULT_KEY = "defaultLayoutID";

    /**
     * 本地用户摄像头状态
     */
    String LOCAL_USER_CAMERA_KEY = "localUserCamera";

    /**
     * 本地用户麦克风
     */
    String LOCAL_USER_MICROPHONE_KEY = "localUserMicrophone";

    /**
     * 本地用户麦克风
     */
    String LOCAL_USER_SOUND_KEY = "localUserSound";

    /**
     * 房间号
     */
    String SERVICE_ROOM_ID = "roomId";
    /**
     * 用户名
     */
    String SERVICE_NAME = "userName";
    /**
     * 用户密码
     */
    String SERVICE_PASSWORD = "userPassword";

}
