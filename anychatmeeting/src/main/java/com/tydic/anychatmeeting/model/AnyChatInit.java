package com.tydic.anychatmeeting.model;

import android.content.Context;

import com.bairuitech.anychat.AnyChatBaseEvent;
import com.bairuitech.anychat.AnyChatCoreSDK;
import com.bairuitech.anychat.AnyChatDefine;
import com.bairuitech.anychat.AnyChatObjectDefine;
import com.bairuitech.anychat.AnyChatStateChgEvent;
import com.bairuitech.anychat.AnyChatTransDataEvent;
import com.tydic.anychatmeeting.bean.EventBusBean;
import com.tydic.anychatmeeting.constant.config.Config;
import com.tydic.anychatmeeting.constant.config.ConfigEntity;
import com.tydic.anychatmeeting.constant.config.ConfigService;

import org.greenrobot.eventbus.EventBus;

/**
 * 作者：like on 2018/6/6 17:25
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：AnyChat的初始化：登录、进入房间等
 */
public class AnyChatInit implements AnyChatBaseEvent, AnyChatStateChgEvent, AnyChatTransDataEvent {

    private Context mContext;

    public static AnyChatCoreSDK anychat;

    private EventBusBean eventBusBean;

    private int roomId;//房间ID

    private String mStrName;//用户名

    private String mStrPwd;//用户密码

    public AnyChatInit(Context mContext) {
        this.mContext = mContext;
        eventBusBean = new EventBusBean();
        anychat = AnyChatCoreSDK.getInstance(mContext);
        setSDKOptionInt();
        setSDKOptionString();
    }

    public AnyChatInit(Context mContext, int roomId, String mStrName, String mStrPwd) {
        this(mContext);
        this.roomId = roomId;
        this.mStrName = mStrName;
        this.mStrPwd = mStrPwd;

    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setStrName(String mStrName) {
        this.mStrName = mStrName;
    }

    public void setStrPwd(String mStrPwd) {
        this.mStrPwd = mStrPwd;
    }

    public void start() {
        initSDK();
    }

    /**
     * 初始化系统
     */
    private void initSDK() {
        anychat.SetBaseEvent(this);
        anychat.SetTransDataEvent(this);
        anychat.SetStateChgEvent(this);
        anychat.InitSDK(android.os.Build.VERSION.SDK_INT, 0);
        connect();
    }

    /**
     * 设置 SDK 整形值相关参数
     */
    private void setSDKOptionInt() {
        settingConfig();
        ConfigEntity configEntity = ConfigService.LoadConfig(mContext);
        if (configEntity.configMode == 1)        // 自定义视频参数配置
        {
            // 设置本地视频编码的码率（如果码率为0，则表示使用质量优先模式）
            AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_BITRATECTRL, configEntity.videoBitrate);
            if (configEntity.videoBitrate == 0) {
                // 设置本地视频编码的质量
                AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_QUALITYCTRL, configEntity.videoQuality);
            }
            // 设置本地视频编码的帧率
            AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_FPSCTRL, configEntity.videoFps);
            // 设置本地视频编码的关键帧间隔
            AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_GOPCTRL, configEntity.videoFps * 4);
            // 设置本地视频采集分辨率
            AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_WIDTHCTRL, configEntity.resolution_width);
            AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_HEIGHTCTRL, configEntity.resolution_height);
            // 设置视频编码预设参数（值越大，编码质量越高，占用CPU资源也会越高）
            AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_PRESETCTRL, configEntity.videoPreset);
        }
        // 让视频参数生效
        AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_APPLYPARAM, configEntity.configMode);
        // P2P设置
        AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_NETWORK_P2PPOLITIC, configEntity.enableP2P);
        // 本地视频Overlay模式设置
        AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_OVERLAY, configEntity.videoOverlay);
        // 回音消除设置
        AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_AUDIO_ECHOCTRL, configEntity.enableAEC);
        // 平台硬件编码设置
        AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_CORESDK_USEHWCODEC, configEntity.useHWCodec);
        // 视频旋转模式设置
        AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_ROTATECTRL, configEntity.videorotatemode);
        // 本地视频采集偏色修正设置
        AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_FIXCOLORDEVIA, configEntity.fixcolordeviation);
        // 视频GPU渲染设置
        AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_VIDEOSHOW_GPUDIRECTRENDER, configEntity.videoShowGPURender);
        // 本地视频自动旋转设置
        AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_AUTOROTATION, configEntity.videoAutoRotation);
    }

    /**
     * 设置 SDK 字符串相关参数
     */
    private void setSDKOptionString() {

    }

    /**
     * 连接服务器
     */
    private void connect() {
        postMessage(eventBusBean.ANYCHAT_START_CONNECT, 0, 0, 0, 0);
        anychat.Connect(Config.mStrIP, Config.mSPort);
    }

    /**
     * 重新连接
     */
    public void resetConnect(){
        connect();
    }

    /**
     * 用户登录系统
     *
     * @param mStrName 用户名
     * @param mStrPwd  密码
     */
    private void login(String mStrName, String mStrPwd) {
        postMessage(eventBusBean.ANYCHAT_START_LOGIN, 0, 0, 0, 0);
        anychat.Login(mStrName, mStrPwd);
    }

    /**
     * 用户登录视频云平台
     */
    private void loginEx() {

    }

    /**
     * 进入房间
     */
    private void enterEoom(int roomId) {
        postMessage(eventBusBean.ANYCHAT_START_ENTER_ROOM, 0, 0, 0, 0);
        anychat.EnterRoom(roomId, "");
    }

    /**
     * 销毁AnyChat
     */
    public static void onDestroy() {
        if (anychat != null){
            anychat.LeaveRoom(-1);
            anychat.Logout();
            anychat.Release();
        }
    }

    /**
     * event发送消息
     *
     * @param type
     * @param dwUserId
     * @param dwRoomId
     * @param dwUserNum
     * @param dwErrorCode
     */
    private void postMessage(int type, int dwUserId, int dwRoomId, int dwUserNum, int dwErrorCode) {
        eventBusBean.type = type;
        eventBusBean.dwUserId = dwUserId;
        eventBusBean.dwRoomId = dwRoomId;
        eventBusBean.dwUserNum = dwUserNum;
        eventBusBean.dwErrorCode = dwErrorCode;
        EventBus.getDefault().post(eventBusBean);
    }

    /**
     * event发送消息
     *
     * @param type
     * @param dwUserId
     * @param dwState
     * @param bOpenMic
     * @param lpBuf
     * @param dwLen
     */
    private void postMessage(int type, int dwUserId, int dwState, boolean bOpenMic, byte[] lpBuf, int dwLen) {
        eventBusBean.type = type;
        eventBusBean.dwUserId = dwUserId;
        eventBusBean.dwState = dwState;
        eventBusBean.bOpenMic = bOpenMic;
        eventBusBean.lpBuf = lpBuf;
        eventBusBean.dwLen = dwLen;
        EventBus.getDefault().post(eventBusBean);
    }

    /**
     * 说明:当客户端连接服务器时被触发，等同于 WIN32 平台的 WM_GV_CONNECT 消息。
     *
     * @param bSuccess 表示是否连接成功，BOOLEAN 类型
     */
    @Override
    public void OnAnyChatConnectMessage(boolean bSuccess) {
        if (bSuccess) {
            postMessage(eventBusBean.ANYCHAT_CONNECT_SUCCESS, 0, 0, 0, 0);
            login(mStrName, mStrPwd);
        } else {
            postMessage(eventBusBean.ANYCHAT_CONNECT_FAIL, 0, 0, 0, 0);
        }
    }

    /**
     * 说明:当客户端登录服务器时被触发，等同于 WIN32 平台的 WM_GV_LOGINSYSTEM 消息。
     *
     * @param dwUserId    表示自己的用户 ID 号,当 dwErrorCode 为 0 时有效
     * @param dwErrorCode 出错代码，可判断登录是否成功
     */
    @Override
    public void OnAnyChatLoginMessage(int dwUserId, int dwErrorCode) {
        if (dwErrorCode == 0) {
            //登录成功
            postMessage(eventBusBean.ANYCHAT_LOGIN_SUCCESS, dwUserId, 0, 0, dwErrorCode);
            initClientObjectInfo(dwUserId);
            enterEoom(roomId);
        } else {
            //登录失败
            postMessage(eventBusBean.ANYCHAT_LOGIN_FAIL, dwUserId, 0, 0, dwErrorCode);
        }
    }

    /**
     * 说明:当客户端请求进入房间时被触发，等同于 WIN32 平台的 WM_GV_ENTERROOM 消息
     *
     * @param dwRoomId    表示进入的房间 ID 号
     * @param dwErrorCode 出错代码，可判断进入房间是否成功
     */
    @Override
    public void OnAnyChatEnterRoomMessage(int dwRoomId, int dwErrorCode) {
        if (dwErrorCode == 0) {
            postMessage(eventBusBean.ANYCHAT_ENTER_ROOM_SUCCESS, 0, dwRoomId, 0, dwErrorCode);
        } else {
            postMessage(eventBusBean.ANYCHAT_ENTER_ROOM_FAIL, 0, dwRoomId, 0, dwErrorCode);
        }
    }

    /**
     * 说明:房间在线用户消息，进入房间后触发一次，等同于 WIN32 平台的 WM_GV_ONLINEUSER 消息。收到该消息后，便可对房间中的用户进行音视频 的相关操作，如请求音频、请求视频等
     *
     * @param dwUserNum 表示当前房间的在线用户数(包含自己)
     * @param dwRoomId  房间编号
     */
    @Override
    public void OnAnyChatOnlineUserMessage(int dwUserNum, int dwRoomId) {
        postMessage(eventBusBean.ANYCHAT_ONLINE_USER_NUM, 0, dwRoomId, dwUserNum, 0);
    }

    /**
     * 说明:当成功进入房间之后，有新的用户进入房间，或是房间用户离开房间，均 会触发该接口，等同于 WIN32 平台的 WM_GV_USERATROOM 消息
     *
     * @param dwUserId 表示当前房间活动用户的 ID 号
     * @param bEnter   true 表示进入房间，false 表示离开房间
     */
    @Override
    public void OnAnyChatUserAtRoomMessage(int dwUserId, boolean bEnter) {
        if (bEnter) {
            postMessage(eventBusBean.ANYCHAT_USER_ENTER_ROOM, dwUserId, 0, 0, 0);
        } else {
            postMessage(eventBusBean.ANYCHAT_USER_EXIT_ROOM, dwUserId, 0, 0, 0);
        }
    }

    /**
     * 说明:当连接服务器成功之后，网络连接关闭时触发该接口，等同于 WIN32 平 台的 WM_GV_LINKCLOSE 消息。
     *
     * @param dwErrorCode 表示连接被关闭的原因
     */
    @Override
    public void OnAnyChatLinkCloseMessage(int dwErrorCode) {
        postMessage(eventBusBean.ANYCHAT_LINK_CLOSE, 0, 0, 0, dwErrorCode);
    }


    /**
     * 当进入房间成功之后，当用户使用 API：UserSpeakControl 操作本地音频设备时将会触发该接口，
     * 等同于 WIN32 平台的 WM_GV_MICSTATECHANGE消息
     *
     * @param dwUserId 表示状态变化的用户 ID
     * @param bOpenMic 表示该用户是否已打开音频采集设备
     */
    @Override
    public void OnAnyChatMicStateChgMessage(int dwUserId, boolean bOpenMic) {
        postMessage(eventBusBean.ANYCHAT_MIC_STATUS_CHG, dwUserId, 0, bOpenMic, null, 0);
    }

    /**
     * 当进入房间成功之后，当用户使用 API：UserCameraControl 操作本地视频设备时将会触发该接口，
     * 等同于 WIN32 平台的 WM_GV_CAMERASTATE 消息
     *
     * @param dwUserId 表示状态变化的用户 ID
     * @param dwState  表示该用户当前的视频设备状态：0 没有摄像头设备
     *                 1 有摄像头设备，但没有打开
     *                 2 已打开摄像头设备
     */
    @Override
    public void OnAnyChatCameraStateChgMessage(int dwUserId, int dwState) {
        postMessage(eventBusBean.ANYCHAT_CAMERA_STATUS_CHG, dwUserId, dwState, false, null, 0);
    }

    /**
     * 当进入房间成功之后，当用户改变聊天模式时将会触发该接口，等同于WIN32 平台的 WM_GV_CHATMODECHG 消息
     *
     * @param dwUserId    表示状态变化的用户 ID
     * @param bPublicChat 表示该用户当前是否为公聊状态，否则为私聊状态
     */
    @Override
    public void OnAnyChatChatModeChgMessage(int dwUserId, boolean bPublicChat) {

    }

    /**
     * 用户活动状态发生变化消息
     *
     * @param dwUserId 表示用户ID号
     * @param dwState  表示用户的当前活动状态
     */
    @Override
    public void OnAnyChatActiveStateChgMessage(int dwUserId, int dwState) {

    }

    /**
     * 当进入房间成功之后，与其它用户建立 P2P 连接，或是 P2P 连接被断开时触发该接口，
     * 等同于 WIN32 平台的 WM_GV_P2PCONNECTSTATE 消息
     *
     * @param dwUserId 表示其它用户 ID 号
     * @param dwState  表示本地用户与其它用户的当前 P2P 网络连接状态：
     *                 0 没有任何连接
     *                 1 P2P 连接成功，TCP 连接
     *                 2 P2P 连接成功，UDP 连接
     *                 3 P2P 连接成功，TCP 与 UDP 连接
     */
    @Override
    public void OnAnyChatP2PConnectStateMessage(int dwUserId, int dwState) {

    }

    /**
     * 当收到其它用户使用“TransFile”方法发送的文件时，将会触发该接口，等同于回调函数：BRAC_TransFile_CallBack。
     * 特别提示：本 SDK 不会删除“lpTempFilePath”所指示的临时文件，上层应用在处理完毕后，需要主动删除该临时文件
     *
     * @param dwUserid     用户 ID，指示发送用户
     * @param FileName     文件名（含扩展名，不含路径）
     * @param TempFilePath 接收完成后，SDK 保存在本地的临时文件（包含完整路径）
     * @param dwFileLength 文件总长度
     * @param wParam       附带参数 1
     * @param lParam       附带参数 2
     * @param dwTaskId     该文件所对应的任务编号
     */
    @Override
    public void OnAnyChatTransFile(int dwUserid, String FileName, String TempFilePath, int dwFileLength, int wParam, int lParam, int dwTaskId) {

    }

    /**
     * 当收到其它用户使用“TransBuffer””方法发送的缓冲区数据时，将会触发该接口，等同于回调函数：BRAC_TransBuffer_CallBack。
     * 由于该函数传递的数据是一个与本 SDK 无关的缓冲区（由上层应用自己填充内容），
     * 相对于本 SDK 来说是透明的，故称为透明通道，利用该通道，可以向当前房间内的任何用户传输上层应用自定义的数据
     *
     * @param dwUserid 用户 ID，指示发送用户
     * @param lpBuf    用户 ID，指示发送用户
     * @param dwLen    缓冲区大小
     */
    @Override
    public void OnAnyChatTransBuffer(int dwUserid, byte[] lpBuf, int dwLen) {
        postMessage(eventBusBean.ANYCHAT_TRANS_BUFFER,dwUserid,0,false,lpBuf,dwLen);
    }

    /**
     * 当收到其它用户使用“TransBufferEx”方法发送的缓冲区数据时，将会触发该接口，等同于回调函数：BRAC_TransBufferEx_CallBack
     *
     * @param dwUserid 用户 ID，指示发送用户
     * @param lpBuf    缓冲区地址
     * @param dwLen    缓冲区大小
     * @param wparam   缓冲区附带参数（由发送者设置，上层应用可自定义用途）
     * @param lparam   缓冲区附带参数 2
     * @param taskid   该缓冲区所对应的传输任务编号
     */
    @Override
    public void OnAnyChatTransBufferEx(int dwUserid, byte[] lpBuf, int dwLen, int wparam, int lparam, int taskid) {

    }

    /**
     * 当收到服务器“SDK Filter”或是“Server SDK”相关接口发送的缓冲区数据时，
     * 将会触发该接口，等同于回调函数：BRAC_SDKFilterData_CallBack
     *
     * @param lpBuf 缓冲区地址
     * @param dwLen 缓冲区大小
     */
    @Override
    public void OnAnyChatSDKFilterData(byte[] lpBuf, int dwLen) {
        postMessage(eventBusBean.ANYCHAT_FILTER_DATA,0,0,false,lpBuf,dwLen);
    }


    /**
     * 设置手机配置
     */
    private void settingConfig() {
        ConfigEntity entity = ConfigService.LoadConfig(mContext);
        //设置配置模式为自定义配置
        entity.configMode = ConfigEntity.VIDEO_MODE_CUSTOMCONFIG;
        //配置视频的宽度
        entity.resolution_width = 640;
        //配置视频的高度
        entity.resolution_height = 480;
        //配置码率
        entity.videoBitrate = 150 * 1000;
        //配置帧率
        entity.videoFps = 10;
        //视屏质量
        entity.videoQuality = ConfigEntity.VIDEO_QUALITY_GOOD;
        //视频预设参数  1-最高效率，较低质量 2-较高效率，较低质量 3-性能均衡（默认） 4-较高质量，较低效率 5-最高质量，较低效率
        entity.videoPreset = 3;
        //本地视频是否采用Overlay模式
        entity.videoOverlay = 1;
        //本地视频旋转模式
        entity.videorotatemode = 0;
        //修正本地视频采集偏色：0 关闭(默认）， 1 开启
        entity.fixcolordeviation = 0;
        //视频数据通过GPU直接渲染：0  关闭(默认)， 1 开启
        entity.videoShowGPURender = 0;
        //本地视频自动旋转控制（参数为int型， 0表示关闭， 1 开启[默认]，视频旋转时需要参考本地视频设备方向参数）
        entity.videoAutoRotation = 0;
        //是否采用P2P网络连接
        entity.enableP2P = 1;
        // 是否强制使用ARMv6指令集，默认是内核自动判断
        entity.useARMv6Lib = 0;
        // 是否使用回音消除功能
        entity.enableAEC = 1;
        // 是否使用平台内置硬件编解码器
        entity.useHWCodec = 0;
        ConfigService.SaveConfig(mContext, entity);
    }

    // 初始化服务对象事件；触发回调OnAnyChatObjectEvent函数
    private void initClientObjectInfo(int dwUserId) {
        // 业务对象身份初始化；0代表普通客户，2是代表座席 (USER_TYPE_ID)
        AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_OBJECT_INITFLAGS, 0);
        // 业务对象优先级设定；
        int dwPriority = 10;
        AnyChatCoreSDK.ObjectSetIntValue(AnyChatObjectDefine.ANYCHAT_OBJECT_TYPE_CLIENTUSER, dwUserId,
                AnyChatObjectDefine.ANYCHAT_OBJECT_INFO_PRIORITY, dwPriority);
        // 业务对象属性设定,必须是-1；
        int dwAttribute = -1;
        AnyChatCoreSDK.ObjectSetIntValue(AnyChatObjectDefine.ANYCHAT_OBJECT_TYPE_CLIENTUSER, dwUserId,
                AnyChatObjectDefine.ANYCHAT_OBJECT_INFO_ATTRIBUTE, dwAttribute);
        // 向服务器发送数据同步请求指令
        AnyChatCoreSDK.ObjectControl(AnyChatObjectDefine.ANYCHAT_OBJECT_TYPE_AREA,
                AnyChatObjectDefine.ANYCHAT_INVALID_OBJECT_ID,
                AnyChatObjectDefine.ANYCHAT_OBJECT_CTRL_SYNCDATA, dwUserId, 0,
                0, 0, "");
    }


}
