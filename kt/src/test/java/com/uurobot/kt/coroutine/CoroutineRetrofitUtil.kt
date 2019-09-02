package com.uurobot.kt.coroutine

import com.canbot.u05.network.Api
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Created by tao.liu on 2019/9/2.
 * 协程+retrofit
 */
object CoroutineRetrofitUtil {
        private val TIMEOUT = 60//超时时间60s

        /**
         * 设置超时
         *
         * @return
         */
        private fun setTimeOut(): OkHttpClient {
                return OkHttpClient.Builder()
                        .connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                        .writeTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                        .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                        .build()

        }

        fun retrofit(): Retrofit {
                return Retrofit.Builder()
                        .baseUrl("http://:7755")
                        .client(setTimeOut())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
        }

        fun requestDataToUI() {
                GlobalScope.launch(Dispatchers.Main) {
                        try {

                                //从这里开始请求网络
                                getXXData() //成功回调
                        } catch (t: Throwable) {
                                //失败回调
                                //to do something
                        }
                }
        }

        suspend fun getXXData() = withContext(Dispatchers.IO) {
                requestData()
        }

        suspend fun requestData(): String = suspendCancellableCoroutine { continuation ->
                //获取当前协程实例
                val queryAllDace = retrofit().create(Api::class.java).queryAllDance("queryAll")
                queryAllDace.enqueue(object : Callback<String> {
                        override fun onFailure(call: Call<String>, t: Throwable) {
                                continuation.resumeWithException(t)
                        }

                        override fun onResponse(call: Call<String>, response: Response<String>) {
                                val body = response.body()

                                if (body != null) {
                                        continuation.resume(body)//这看看源码也能大概知道什么意思，还有个resumeWith也可以了解一下
                                } else {
                                        continuation.resumeWithException(NullPointerException("Response Body is Null : $response"))
                                }
                        }
                })
        }

}