package com.canbot.coroutinekt

import kotlinx.coroutines.*
import org.junit.Test

/**
 * Created by tao.liu on 2018/12/20.
 * 协程基础
 */
class CoroutinesUnitTest {
        @Test
        fun test01() {
                GlobalScope.launch {
                        // 在后台启动一个新的协程并继续
                        delay(1000L) // 无阻塞的等待1秒钟（默认时间单位是毫秒）
                        println("World!") // 在延迟后打印输出
                }
                println("Hello,") // 主线程的协程将会继续等待
                Thread.sleep(2000L) // 阻塞主线程2秒钟来保证 JVM 存活
        }

        /**
         * 结果是相似的，但是这些代码只使用了非阻塞的函数 delay。
         * 在主线程中调用了 runBlocking， 阻塞 会持续到 runBlocking 中的协程执行完毕。
         */
        @Test
        fun test02() {
                GlobalScope.launch {
                        // 在后台启动一个新的协程并继续
                        delay(1000L)
                        println("World!")
                }
                println("Hello,") // 主线程中的代码会立即执行
                runBlocking {
                        // 但是这个函数阻塞了主线程
                        delay(2000L)  // ……我们延迟2秒来保证 JVM 的存活
                }
        }

        /**
         * 这个例子可以使用更多的惯用方法来重写，使用 runBlocking 来包装 main 函数:
         */
        @Test
        fun test03() = runBlocking<Unit> {
                // 开始执行主协程
                GlobalScope.launch {
                        // 在后台开启一个新的协程并继续
                        delay(1000L)
                        println("World!")
                }
                println("Hello,") // 主协程在这里会立即执行
                delay(2000L)      // 延迟2秒来保证 JVM 存活
        }

        /**
         * 等待一个任务
         * 延迟一段时间来等待另一个协程开始工作并不是一个好的选择。
         * 让我们显式地等待（使用非阻塞的方法）一个后台 Job 执行结束:
         */
        @Test
        fun test04() = runBlocking {
                val job = GlobalScope.launch {
                        // 启动一个新的协程并保持对这个任务的引用
                        delay(1000L)
                        println("World!")
                }
                println("Hello,")
                job.join() // 等待直到子协程执行结束
        }

        /**
         * 结构化的并发
         * 每一个协程构建器，包括 runBlocking， 在它代码块的作用域内添加一个CoroutineScope 实例。
         * 在这个作用域内启动的协程不需要明确的调用 join，因为一个外围的协程（我们的例子中的 runBlocking）
         * 只有在它作用域内所有协程执行完毕后才会结束
         */
        @Test
        fun test05() = runBlocking {
                // this: CoroutineScope
                launch {
                        // 在 runBlocking 作用域中启动一个新协程
                        delay(1000L)
                        println("World!")
                }
                println("Hello,")
        }

        /**
         * 作用域构建器???
         * 除了由上面多种构建器提供的协程作用域，也是可以使用 coroutineScope 构建器来声明你自己的作用域的。
         * 它启动了一个新的协程作用域并且在所有子协程执行结束后并没有执行完毕。
         * runBlocking 和 coroutineScope 主要的不同之处在于后者在等待所有的子协程执行完毕时并没有使当前线程阻塞。
         */
        @Test
        fun test06() = runBlocking {
                // this: CoroutineScope
                launch {
                        delay(200L)
                        println("Task from runBlocking")
                }

                coroutineScope {
                        // 创建一个新的协程作用域
                        launch {
                                delay(500L)
                                println("Task from nested launch")
                        }

                        delay(100L)
                        println("Task from coroutine scope") // 该行将在嵌套启动之前执行打印
                }

                println("Coroutine scope is over") // 该行将在嵌套结束之后才会被打印
        }

        /**
         * 提取函数重构
         */
        @Test
        fun test07() = runBlocking {
                launch {
                        doWorld()
                }
                println("Hello,")
        }

        /**
         * 这里是一个挂起函数
         */
        private suspend fun doWorld() {
                delay(1000L)
                println("World!")
        }

        /**
         * 协程是轻量级的
         */
        @Test
        fun test08() {
                runBlocking {
                        val timeMillis = System.currentTimeMillis()
                        repeat(100_000) {
                                // 启动大量的协程
                                launch {
                                        delay(1000L)
                                        print("." + it)
                                }
                                println("${System.currentTimeMillis() - timeMillis}s")
                        }
                }
        }

        /**
         * 协程的区别 轻量级
         */
        @Test
        fun test09() {
                var i = 100_000
                val timeMillis = System.currentTimeMillis()
                while (i > 0) {
                        Thread(Runnable {
                                Thread.sleep(1000)
                                print(".")
                        }).start()
                        i--
                }
                println("---")
                println("总共用时 ${System.currentTimeMillis() - timeMillis}s")
        }

        /**
         * 像守护线程一样的全局协程
         */
        @Test
        fun test10() = runBlocking {
                GlobalScope.launch {
                        repeat(1000) { i ->
                                println("I'm sleeping $i ...")
                                delay(500L)
                        }
                }
                // 在延迟之后结束程序
                delay(1800L)
        }
        ////////////////////////////////////取消与超时///////////////////////////////////////////////

        /**
         * 取消协程的执行
         */
        @Test
        fun test11() = runBlocking {
                val job = launch {
                        repeat(1000) { i ->
                                println("I'm sleeping $i ...")
                                delay(500L)
                        }
                }
                delay(1300L) // 延迟一段时间
                println("main: I'm tired of waiting!")
                //job.cancel() // 取消该任务
                //job.join() // 等待任务执行结束
                //使 Job 挂起的函数 cancelAndJoin 它合并了对 cancel 以及 join 的调用。
                job.cancelAndJoin()
                println("main: Now I can quit.")
        }

        /**
         * 取消是协作的
         */
        @Test
        fun test12() = runBlocking {
                val startTime = System.currentTimeMillis()
                val job = launch(Dispatchers.Default) {
                        var nextPrintTime = startTime
                        var i = 0
                        while (i < 5) { // 一个执行计算的循环，只是为了占用CPU
                                // 每秒打印消息两次
                                if (System.currentTimeMillis() >= nextPrintTime) {
                                        println("I'm sleeping ${i++} ...")
                                        nextPrintTime += 500L
                                }
                        }
                }
                delay(1300L) // 等待一段时间
                println("main: I'm tired of waiting!")
                job.cancelAndJoin() // 取消一个任务并且等待它结束
                println("main: Now I can quit.")
        }

        /**
         * 使计算代码可取消
         */
        @Test
        fun test13() = runBlocking {
                val startTime = System.currentTimeMillis()
                val job = launch(Dispatchers.Default) {
                        var nextPrintTime = startTime
                        var i = 0
                        while (isActive) { // 可以被取消的计算循环
                                // 每秒打印消息两次
                                if (System.currentTimeMillis() >= nextPrintTime) {
                                        println("I'm sleeping ${i++} ...")
                                        nextPrintTime += 500L
                                }
                        }
                }
                delay(1300L) // 等待一段时间
                println("main: I'm tired of waiting!")
                job.cancelAndJoin() // 取消该任务并等待它结束
                println("main: Now I can quit.")
        }

        /**
         * 在 finally 中释放资源
         */
        @Test
        fun test14() = runBlocking {
                val job = launch {
                        try {
                                repeat(1000) { i ->
                                        println("I'm sleeping $i ...")
                                        delay(500L)
                                }
                        } finally {
                                println("I'm running finally")
                        }
                }
                delay(1300L) // 延迟一段时间
                println("main: I'm tired of waiting!")
                job.cancelAndJoin() // 取消该任务并且等待它结束
                println("main: Now I can quit.")
        }

        /**
         * 运行不能取消的代码块
         */
        @Test
        fun test15() = runBlocking {
                val job = launch {
                        try {
                                repeat(1000) { i ->
                                        println("I'm sleeping $i ...")
                                        delay(500L)
                                }
                        } finally {
                                withContext(NonCancellable) {
                                        println("I'm running finally")
                                        delay(1000L)
                                        println("And I've just delayed for 1 sec because I'm non-cancellable")
                                }
                        }
                }
                delay(1300L) // 延迟一段时间
                println("main: I'm tired of waiting!")
                job.cancelAndJoin() // 取消该任务并等待它结束
                println("main: Now I can quit.")
        }

        /**
         * 超时
         */
        @Test
        fun test16() = runBlocking {
                withTimeout(1300L) {
                        repeat(1000) { i ->
                                println("I'm sleeping $i ...")
                                delay(500L)
                        }
                }
        }

        @Test
        fun test17()= runBlocking {
                val result = withTimeoutOrNull(1300L) {
                        repeat(1000) { i ->
                                println("I'm sleeping $i ...")
                                delay(500L)
                        }
                        "Done" // 在它运行得到结果之前取消它
                }
                println("Result is $result")
        }

}