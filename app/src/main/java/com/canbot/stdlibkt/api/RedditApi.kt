package com.canbot.stdlibkt.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by tao.liu on 2018/9/25.
 * description this is description
 */
interface RedditApi {
        @GET("/top.json")
        fun getTop(@Query("after") after: String,
                   @Query("limit") limit: String)
                : Call<RedditNewsResponse>
}