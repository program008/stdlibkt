package com.uurobot.kt.coroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by tao.liu on 2019/9/2.
 * description this is description
 */
class CoroutineUnitTest4 {


        /**
         * 1.根据url地址下载播放列表, 网络访问，运行后台
         */
        fun _download(url : String) : String {
                println("根据url下载播放节目表 thread.name = ${Thread.currentThread().name} , id = ${Thread.currentThread().id}")
                return "节目表"
        }

        /**
         * 2.先解析下载好播放列表文件中布局相关信息,协议解析，运行后台
         */
        fun _parseLayout(filePath : String) : String {
                println("先解析节目表界面布局 thread.name = ${Thread.currentThread().name} , id = ${Thread.currentThread().id}")
                return "界面布局信息"
        }

        /**
         * 3.根据布局信息先绘制出界面框架,界面绘制，运行主线程
         */
        fun _drawLayout(layoutInfo : String) : String {
                println("绘制ui界面布局 thread.name = ${Thread.currentThread().name} , id = ${Thread.currentThread().id}")
                return "布局绘制完成"
        }

        /**
         * 4.接着解析播放列表中的播放素材列表,解析协议，运行后台
         */
        fun _parsePlayList(filePath : String) : String {
                println("继续解析节目单播放的素材内容 thread.name = ${Thread.currentThread().name} , id = ${Thread.currentThread().id}")
                return "播放素材列表"
        }

        /**
         * 5.界面上播放多媒体素材,运行主线程
         */
        fun _startPlay(playList : String) : String {
                println("播放多媒体素材 thread.name = ${Thread.currentThread().name} , id = ${Thread.currentThread().id}")
                return "播放成功"
        }

        /**
         * 6.反馈平台播放结果,网络访问，运行后台
         */
        fun _notifyResult() {
                println("反馈平台播放结果 thread.name = ${Thread.currentThread().name} , id = ${Thread.currentThread().id}")
        }

        /**
         * 使用rxjava的方式，能够省得一大堆自定义的结果回调，以及带来方便的线程切换功能
         */



        //通过lambda表达式优化下
        @Test
        fun rxjavaLambda() {
                Observable.just("http://......")
                        //后台线程
                        .map { url -> _download(url) }
                        //后台线程
                        .map { filePath -> _parseLayout(filePath) }
                        .observeOn(AndroidSchedulers.mainThread())
                        //UI线程
                        .map { layoutInfo -> _drawLayout(layoutInfo) }
                        .observeOn(Schedulers.newThread())
                        //后台线程
                        .map { filePath -> _parsePlayList(filePath) }
                        .observeOn(AndroidSchedulers.mainThread())
                        //UI线程
                        .map { playList -> _startPlay(playList) }
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        //后台线程
                        .subscribe{isSuccess -> _notifyResult()}
        }

        @Test
        fun coroutines() {
                //async默认最后一行是返回值，所以return@async都可以去掉
                runBlocking {
                        //后台线程
                        val filePath = async(Dispatchers.Default) {  _download("http://...")}.await()
                        //后台线程
                        val layoutInfo = async(Dispatchers.IO) { _parseLayout(filePath) }.await()
                        //这边是与上边rxjava方案相比最大的不同，可以实现_drawLayout与_parsePlayList同步进行，效率更高
                        //UI线程
                        launch(Dispatchers.Main) { _drawLayout(layoutInfo) }
                        //后台线程
                        val playList = async(Dispatchers.IO) { _parsePlayList(filePath) }.await()
                        //UI线程
                        val isSuccess = async(Dispatchers.Main) { _startPlay(playList) }.await()
                        //后台线程
                        launch(Dispatchers.Default) { _notifyResult() }
                }
        }


}