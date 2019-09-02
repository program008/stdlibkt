package com.uurobot.kt.coroutine

import android.os.Handler
import android.os.Looper
import org.junit.Test
import kotlin.concurrent.thread

/**
 * Created by tao.liu on 2019/9/2.
 * description this is description
 */
class CoroutineUnitTest3 {
        val mainHandler = Handler(Looper.getMainLooper())

        interface CallBack {
                fun onSuccess(response: String)
        }

        /**
         * 1.根据url地址下载播放列表, 网络访问，运行后台
         */
        fun download(url: String, callBack: CallBack) {
                thread {
                        println("根据url下载播放节目表 thread.name = ${Thread.currentThread().name} , id = ${Thread.currentThread().id}")
                        callBack.onSuccess("节目表")
                }
        }

        /**
         * 2.先解析下载好播放列表文件中布局相关信息,协议解析，运行后台
         */
        fun parseLayout(filePath: String, callBack: CallBack) {
                thread {
                        println("先解析节目表界面布局 thread.name = ${Thread.currentThread().name} , id = ${Thread.currentThread().id}")
                        callBack.onSuccess("界面布局信息")
                }
        }

        /**
         * 3.根据布局信息先绘制出界面框架,界面绘制，运行主线程
         */
        fun drawLayout(layoutInfo: String, callBack: CallBack) {
                mainHandler.post(Runnable {
                        println("绘制ui界面布局 thread.name = ${Thread.currentThread().name} , id = ${Thread.currentThread().id}")
                        callBack.onSuccess("布局绘制完成")
                })
        }

        /**
         * 4.接着解析播放列表中的播放素材列表,解析协议，运行后台
         */
        fun parsePlayList(filePath: String, callBack: CallBack) {
                thread {
                        println("继续解析节目单播放的素材内容 thread.name = ${Thread.currentThread().name} , id = ${Thread.currentThread().id}")
                        callBack.onSuccess("播放素材列表")
                }
        }

        /**
         * 5.界面上播放多媒体素材,运行主线程
         */
        fun startPlay(playList: String, callBack: CallBack) {
                mainHandler.post(Runnable {
                        println("播放多媒体素材 thread.name = ${Thread.currentThread().name} , id = ${Thread.currentThread().id}")
                        callBack.onSuccess("播放成功")
                })
        }

        /**
         * 6.反馈平台播放结果,网络访问，运行后台
         */
        fun notifyResult() {
                thread {
                        println("反馈平台播放结果 thread.name = ${Thread.currentThread().name} , id = ${Thread.currentThread().id}")
                }
        }

        /**
         * android原生方式, 回调一旦太多，就会陷入回调地狱。。。
         *
         * 并且是把线程控制封装在相应的api中，一旦线程控制放在外面，则更加难以理解
         */
        @Test
        fun android() {

                download("http://....", object : CallBack {
                        override fun onSuccess(filePath: String) {
                                parseLayout(filePath, object : CallBack {
                                        override fun onSuccess(layoutInfo: String) {
                                                drawLayout(layoutInfo, object : CallBack {
                                                        override fun onSuccess(filePath: String) {
                                                                parsePlayList(filePath, object : CallBack {
                                                                        override fun onSuccess(playList: String) {
                                                                                startPlay(playList, object : CallBack {
                                                                                        override fun onSuccess(response: String) {
                                                                                                notifyResult()
                                                                                        }
                                                                                })
                                                                        }
                                                                })
                                                        }
                                                })
                                        }
                                })
                        }
                })
        }

}