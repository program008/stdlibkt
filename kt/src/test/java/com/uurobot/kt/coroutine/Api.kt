package com.canbot.u05.network
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by tao.liu on 2019/6/28.
 * 舞蹈相关api
 */
interface Api {
        /**
         * 查询所有舞蹈
         */
        @FormUrlEncoded
        @POST("danceManager")
        fun queryAllDance(@Field("queryAll") json: String): Call<String>
}