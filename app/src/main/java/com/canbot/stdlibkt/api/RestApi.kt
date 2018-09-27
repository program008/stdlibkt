package com.canbot.stdlibkt.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by tao.liu on 2018/9/25.
 * description this is description
 */
class RestApi {
        private val redditApi: RedditApi

        init {
                val retrofit = Retrofit.Builder()
                        .baseUrl("https://www.reddit.com")
                        .addConverterFactory(MoshiConverterFactory.create())
                        .build()

                redditApi = retrofit.create(RedditApi::class.java)
        }
}