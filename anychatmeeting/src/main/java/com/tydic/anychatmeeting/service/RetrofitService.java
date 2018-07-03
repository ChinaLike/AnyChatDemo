package com.tydic.anychatmeeting.service;

import com.tydic.anychatmeeting.base.BaseBean;
import com.tydic.anychatmeeting.bean.DocumentBean;
import com.tydic.anychatmeeting.bean.UsersBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 网络请求接口
 * Created by like on 2017-09-19
 */

public interface RetrofitService {

    /**
     * 获取在线人员列表
     * <p>
     * roomId
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("anychat/{roomId}/userlist")
    Call<BaseBean<List<UsersBean>>> getOnlinePeoPle(@Path("roomId") String roomId);

    /**
     * 给某个用户发送消息
     * <p>
     * userId
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("anychat/{userId}/message/{character}")
    Call<BaseBean<String>> sendMsg(@Path("userId") int userId,
                                   @Path("character") String character);

    /**
     * 获取用户状态
     *
     * @param roomId
     * @param yhyUserId
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("anychat/{roomId}/lastuserstate/{yhyUserId}")
    Call<BaseBean<UsersBean>> getUserState(@Path("roomId") String roomId,
                                           @Path("yhyUserId") String yhyUserId);


    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("api/meetings/{meetingId}/meeting-materials")
    Call<BaseBean<List<DocumentBean>>> getMeetingMaterials(@Path("meetingId") String meetingId);

}
