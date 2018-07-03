package com.tydic.anychatmeeting.model;

import android.text.TextUtils;

import com.tydic.anychatmeeting.base.BaseBean;
import com.tydic.anychatmeeting.bean.UsersBean;
import com.tydic.anychatmeeting.constant.Key;
import com.tydic.anychatmeeting.constant.config.Config;
import com.tydic.anychatmeeting.helper.RetrofitCreateHelper;
import com.tydic.anychatmeeting.model.inf.OnRequestListener;
import com.tydic.anychatmeeting.service.RetrofitService;
import com.tydic.anychatmeeting.util.DelayUtil;
import com.tydic.anychatmeeting.util.T;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by like on 2017/12/28.
 */

public class RequestData {

    /**
     * 获取在线人员列表
     *
     * @param roomId 房间ID
     */
    public static void onLineUsers(final int roomId, boolean isDelay, final OnRequestListener listener) {
        DelayUtil.delay(isDelay ? 2000 : 0, new DelayUtil.DelayHelper() {
            @Override
            public void onSuccess() {
                Call<BaseBean<List<UsersBean>>> call = RetrofitCreateHelper
                        .createApi(RetrofitService.class, Config.BASE_URL_ANYCHAT)
                        .getOnlinePeoPle(roomId + "");
                call.enqueue(new Callback<BaseBean<List<UsersBean>>>() {
                    @Override
                    public void onResponse(Call<BaseBean<List<UsersBean>>> call, Response<BaseBean<List<UsersBean>>> response) {
                        BaseBean<List<UsersBean>> baseBean = response.body();
                        if (listener != null) {
                            if (baseBean != null && baseBean.getData() != null) {
                                //返回数据成功
                                listener.onSuccess(Key.ON_LINE_USER, baseBean.getData());
                            } else {
                                //返回数据失败
                                listener.onError(Key.ON_LINE_USER, Key.FAIL);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseBean<List<UsersBean>>> call, Throwable t) {
                        if (listener != null) {
                            listener.onError(Key.ON_LINE_USER, Key.OVER_TIME);
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取指定用户信息
     * @param roomId
     * @param isDelay
     * @param listener
     * @param userId
     */
    public static void getSpecifiedUserInfo(final int roomId, boolean isDelay, final OnRequestListener listener, final int userId){
        onLineUsers(roomId, isDelay, new OnRequestListener() {
            @Override
            public void onSuccess(int type, Object obj) {
                List<UsersBean> list = (List<UsersBean>) obj;
                for (UsersBean bean : list) {
                    int dwUserId = bean.getUserId();
                    if (dwUserId == userId && listener != null){
                        listener.onSuccess(Key.ON_LINE_USER,bean);
                        return;
                    }
                }
            }

            @Override
            public void onError(int type, int code) {
                if (listener != null){
                    listener.onError(Key.ON_LINE_USER, Key.FAIL);
                }
            }
        });
    }

    /**
     * 获取用户状态
     *
     * @param roomId   房间id
     * @param feedId
     * @param listener
     */
    public static void userState(int roomId, String feedId, final OnRequestListener listener) {
        Call<BaseBean<UsersBean>> call = RetrofitCreateHelper
                .createApi(RetrofitService.class, Config.BASE_URL_ANYCHAT)
                .getUserState(roomId + "", feedId);
        call.enqueue(new Callback<BaseBean<UsersBean>>() {
            @Override
            public void onResponse(Call<BaseBean<UsersBean>> call, Response<BaseBean<UsersBean>> response) {
                BaseBean<UsersBean> baseBean = response.body();
                if (listener != null) {
                    if (baseBean != null && baseBean.getData() != null) {
                        listener.onSuccess(Key.USER_STATE, baseBean.getData());
                    } else {
                        listener.onError(Key.USER_STATE, Key.FAIL);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseBean<UsersBean>> call, Throwable t) {
                if (listener != null) {
                    listener.onError(Key.USER_STATE, Key.OVER_TIME);
                }
            }
        });

    }

    /**
     * 向服务器发送消息
     *
     * @param character
     * @param listener
     */
    public static void sendMsg(String character, final UsersBean usersBean, final int type, final OnRequestListener listener) {
        Call<BaseBean<String>> call = RetrofitCreateHelper
                .createApi(RetrofitService.class, Config.BASE_URL_ANYCHAT)
                .sendMsg(usersBean.getUserId(), character);
        call.enqueue(new Callback<BaseBean<String>>() {
            @Override
            public void onResponse(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                BaseBean<String> baseBean = response.body();
                if (!TextUtils.isEmpty(baseBean.getData())) {
                    if (listener != null) {
                        listener.onSuccess(type, usersBean);
                    }
                } else {
                    T.showShort(baseBean.getData());
                    if (listener != null) {
                        listener.onError(type, 0);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseBean<String>> call, Throwable t) {
                if (listener != null) {
                    listener.onError(type, Key.OVER_TIME);
                }
            }
        });
    }
}
