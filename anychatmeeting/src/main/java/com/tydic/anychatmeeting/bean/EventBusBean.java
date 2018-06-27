package com.tydic.anychatmeeting.bean;

/**
 * 作者：like on 2018/6/7 10:10
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：事件传输对象，即定义EventBus的传输数据
 */
public class EventBusBean {
    public final static int ANYCHAT_INIT = 0;//初始化anychat
    public final static int ANYCHAT_START_CONNECT = 1;//anychat开始连接
    public final static int ANYCHAT_CONNECT_SUCCESS = 2;//anychat连接成功
    public final static int ANYCHAT_CONNECT_FAIL =3;//anychat连接失败
    public final static int ANYCHAT_START_LOGIN = 4;//anychat开始登录
    public final static int ANYCHAT_LOGIN_SUCCESS = 5;//anychat登录失败
    public final static int ANYCHAT_LOGIN_FAIL = 6;//anychat登录成功
    public final static int ANYCHAT_START_ENTER_ROOM = 7;//anychat开始进入房间
    public final static int ANYCHAT_ENTER_ROOM_SUCCESS= 8;//anychat进入房间成功
    public final static int ANYCHAT_ENTER_ROOM_FAIL = 9;//anychat进入房间失败
    public final static int ANYCHAT_ONLINE_USER_NUM = 10;//anychat在线用户数
    public final static int ANYCHAT_USER_ENTER_ROOM = 11;//用户进入房间
    public final static int ANYCHAT_USER_EXIT_ROOM = 12;//用户退出房间
    public final static int ANYCHAT_LINK_CLOSE =13;//网络关闭

    public final static int ANYCHAT_CAMERA_STATUS_CHG =14;//用户摄像头改变通知
    public final static int ANYCHAT_MIC_STATUS_CHG =15;//用户麦克风状态改变通知
    public final static int ANYCHAT_FILTER_DATA =16;//接受过滤消息
    public final static int ANYCHAT_TRANS_BUFFER =17;//透明通道

    public int type;
    public int dwUserId;//用户ID
    public int dwRoomId;//房间ID
    public int dwUserNum;//当前房间在线用户数
    public int dwErrorCode;//出错代码
    public int dwState;//用户设备状态
    public boolean bOpenMic;//用户麦克风状态
    public byte[] lpBuf;//缓冲区地址
    public int dwLen;//缓冲区大小
}
