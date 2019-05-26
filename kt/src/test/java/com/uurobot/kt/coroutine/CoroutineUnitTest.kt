package com.uurobot.kt.coroutine

import kotlinx.coroutines.*
import org.junit.Test

/**
 *
 * Created by tao.liu on 2019/5/26.
 * 协程基础
 *
 * 守护线程：
 * Java的线程分为两种：User Thread(用户线程)、DaemonThread(守护线程)。
 * 只要当前JVM实例中尚存任何一个非守护线程没有结束，守护线程就全部工作；
 * 只有当最后一个非守护线程结束是，守护线程随着JVM一同结束工作，Daemon作用是为其他线程提供便利服务，
 * 守护线程最典型的应用就是GC(垃圾回收器)，他就是一个很称职的守护者。
 *
 * User和Daemon两者几乎没有区别，唯一的不同之处就在于虚拟机的离开：
 * 如果 User Thread已经全部退出运行了，只剩下Daemon Thread存在了，虚拟机也就退出了。
 * 因为没有了被守护者，Daemon也就没有工作可做了，也就没有继续运行程序的必要了。
 */

class CoroutineUnitTest {
        @Test
        fun coroutine1() {
                GlobalScope.launch {
                        // launch a new coroutine in background and continue
                        delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
                        println("World!") // print after delay
                }
                println("Hello,") // main thread continues while coroutine is delayed
                Thread.sleep(2000L) // block main thread for 2 seconds to keep JVM alive
        }

        @Test
        fun coroutine2() {
                GlobalScope.launch {
                        // launch a new coroutine in background and continue
                        delay(1000L)
                        println("World!")
                }
                println("Hello,") // main thread continues here immediately
                runBlocking {
                        // but this expression blocks the main thread
                        delay(2000L)  // ... while we delay for 2 seconds to keep JVM alive
                }
        }

        @Test
        fun coroutine3() = runBlocking<Unit> {
                // start main coroutine
                GlobalScope.launch {
                        // launch a new coroutine in background and continue
                        delay(1000L)
                        println("World!")
                }
                println("Hello,") // main coroutine continues here immediately
                delay(2000L)      // delaying for 2 seconds to keep JVM alive
        }

        @Test
        fun coroutine4() = runBlocking {
                //sampleStart
                val job = GlobalScope.launch {
                        // launch a new coroutine and keep a reference to its Job
                        delay(1000L)
                        println("World!")
                }
                println("Hello,")
                job.join() // wait until child coroutine completes
                //sampleEnd
        }

        /**
         * 结构化的并发
         */
        @Test
        fun coroutine5() = runBlocking {
                // this: CoroutineScope
                launch {
                        // launch a new coroutine in the scope of runBlocking
                        delay(1000L)
                        println("World!")
                }
                println("Hello,")
        }

        /**
         * 作用域构建器
         * runBlocking 与 coroutineScope 的主要区别在于后者在等待所有子协程执行完毕时不会阻塞当前线程。
         */
        @Test
        fun coroutine6() = runBlocking {
                // this: CoroutineScope
                launch {
                        delay(200L)
                        println("Task from runBlocking")
                }

                coroutineScope {
                        // Creates a coroutine scope
                        launch {
                                delay(500L)
                                println("Task from nested launch")
                        }

                        delay(100L)
                        println("Task from coroutine scope") // This line will be printed before the nested launch
                }
                println("Coroutine scope is over") // This line is not printed until the nested launch completes

        }

        @Test
        fun coroutine7() = runBlocking {
                launch { doWorld() }
                println("Hello,")
        }


        // this is your first suspending function
        suspend fun doWorld() {
                delay(1000L)
                println("World!")
        }

        /**
         * 全局协程像[ 守护线程 ]
         */
        @Test
        fun coroutine8() = runBlocking {
                //sampleStart
                GlobalScope.launch {
                        repeat(1000) { i ->
                                println("I'm sleeping $i ...")
                                delay(500L)
                        }
                }
                delay(9300L) // just quit after delay
                //sampleEnd
        }

        @Test fun `ensure everything works`() {
                println("当且仅当测试的时候使用")
        }

        @Test fun ensureEverythingWorks_onAndroid() { TODO() }
}