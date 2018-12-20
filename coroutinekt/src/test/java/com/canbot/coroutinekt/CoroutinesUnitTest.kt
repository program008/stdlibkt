package com.canbot.coroutinekt

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
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
        fun test17() = runBlocking {
                val result = withTimeoutOrNull(1300L) {
                        repeat(1000) { i ->
                                println("I'm sleeping $i ...")
                                delay(500L)
                        }
                        "Done" // 在它运行得到结果之前取消它
                }
                println("Result is $result")
        }

        /////////////////////////////////////通道(实验性的)////////////////////////////////////////////////

        @Test
        fun test18() = runBlocking {
                val channel = Channel<Int>()
                launch {
                        // 这里可能是消耗大量CPU运算的异步逻辑，我们将仅仅做5次整数的平方并发送
                        for (x in 1..5) channel.send(x * x)
                }
                // 这里我们打印了5次被接收的整数：
                repeat(5) { println(channel.receive()) }
                println("Done!")
        }

        /**
         * 关闭和迭代通道
         */
        @Test
        fun test19() = runBlocking {
                val channel = Channel<Int>()
                launch {
                        for (x in 1..5) channel.send(x * x)
                        channel.close() // 我们结束发送
                }
                // 这里我们使用 `for` 循环来打印所有被接收到的元素（直到通道被关闭）
                for (y in channel) println(y)
                println("Done!")
        }

        /**
         * 构建通道生产者
         */
        fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
                for (x in 1..5) send(x * x)
        }

        @Test
        fun test20() = runBlocking {
                val squares = produceSquares()
                squares.consumeEach { println(it) }
                println("Done!")
        }

        /**
         * 管道
         */
        @Test
        fun test21() = runBlocking {
                val numbers = produceNumbers() // 从1开始生产整数
                val squares = square(numbers) // 对整数做平方
                for (i in 1..5) println(squares.receive()) // 打印前5个数字
                println("Done!") // 我们的操作已经结束了
                coroutineContext.cancelChildren() // 取消子协程
        }

        fun CoroutineScope.produceNumbers() = produce<Int> {
                var x = 1
                while (true) send(x++) // 从1开始的无限的整数流
        }

        fun CoroutineScope.square(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
                for (x in numbers) send(x * x)
        }

        @Test
        fun test22() = runBlocking {
                var cur = numbersFrom(2)
                for (i in 1..10) {
                        val prime = cur.receive()
                        println(prime)
                        cur = filter(cur, prime)
                }
                coroutineContext.cancelChildren() // 取消所有的子协程来让主协程结束
        }

        fun CoroutineScope.numbersFrom(start: Int) = produce<Int> {
                var x = start
                while (true) send(x++) // 从 start 开始过滤整数流
        }

        fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int) = produce<Int> {
                for (x in numbers) if (x % prime != 0) send(x)
        }

        /**
         * 扇出
         * 多个协程也许会接收相同的管道，在它们之间进行分布式工作
         */
        @Test
        fun test23() = runBlocking<Unit> {
                val producer = produceNumbers2()
                repeat(5) { launchProcessor(it, producer) }
                delay(950)
                producer.cancel() // 取消协程处理器从而将它们全部杀死
        }

        fun CoroutineScope.produceNumbers2() = produce<Int> {
                var x = 1 // start from 1
                while (true) {
                        send(x++) // 产生下一个数字
                        delay(100) // 等待0.1秒
                }
        }

        fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {
                for (msg in channel) {
                        println("Processor #$id received $msg")
                }
        }

        /**
         * 扇入
         * 多个协程可以发送到同一个通道
         */
        @Test
        fun test24() = runBlocking {
                val channel = Channel<String>()
                launch { sendString(channel, "foo", 200L) }
                launch { sendString(channel, "BAR!", 300L) }
                repeat(6) {
                        // 接收前六个
                        println(channel.receive())
                }
                coroutineContext.cancelChildren() // 取消所有子协程来让主协程结束
        }

        suspend fun sendString(channel: SendChannel<String>, s: String, time: Long) {
                while (true) {
                        delay(time)
                        channel.send(s)
                }
        }

        /**
         * 带缓存的通道
         */
        @Test
        fun test25() = runBlocking<Unit> {
                val channel = Channel<Int>(4) // 启动带缓冲的通道
                val sender = launch {
                        // 启动发送者协程
                        repeat(10) {
                                println("Sending $it") // 在每一个元素发送前打印它们
                                channel.send(it) // 将在缓冲区被占满时挂起
                        }
                }
                // 没有接收到东西……只是等待……
                delay(1000)
                sender.cancel() // 取消发送者协程
        }

        data class Ball(var hits: Int)

        /**
         * 通道是公平的
         * 发送和接收操作是 公平的 并且尊重调用它们的多个协程。它们遵守先进先出原则，
         * 可以看到第一个协程调用 receive 并得到了元素。在下面的例子中两个协程 “乒” 和 "乓" 都从共享的“桌子”通道接收到这个“球”元素。
         *
         * 乒”协程首先被启动，所以它首先接收到了球。甚至虽然“乒” 协程在将球发送会桌子以后立即开始接收，
         * 但是球还是被“乓” 协程接收了，因为它一直在等待着接收球：
         */
        @Test
        fun test26() = runBlocking {
                val table = Channel<Ball>() // 一个共享的table（桌子）
                launch { player("ping", table) }
                launch { player("pong", table) }
                table.send(Ball(0)) // 乒乓球
                delay(1000) // 延迟1秒钟
                coroutineContext.cancelChildren() // 游戏结束，取消它们
        }

        suspend fun player(name: String, table: Channel<Ball>) {
                for (ball in table) { // 在循环中接收球
                        ball.hits++
                        println("$name $ball")
                        delay(300) // 等待一段时间
                        table.send(ball) // 将球发送回去
                }
        }

        /**
         * 计时器通道
         * 计时器通道是一种特别的会合通道，每次经过特定的延迟都会从该通道进行消费并产生 Unit。
         * 虽然它看起来似乎没用，它被用来构建分段来创建复杂的基于时间的 produce 管道和进行窗口化操作以及其它时间相关的处理。
         * 可以在 select 中使用计时器通道来进行“打勾”操作。
         * 使用工厂方法 ticker 来创建这些通道。
         * 为了表明不需要其它元素，请使用 ReceiveChannel.cancel 方法。
         * 现在让我们看看它是如何在实践中工作的：
         */
        @Test
        fun test27() = runBlocking<Unit> {
                val tickerChannel = ticker(delayMillis = 100, initialDelayMillis = 0) //创建计时器通道
                var nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
                println("Initial element is available immediately: $nextElement") // 初始尚未经过的延迟

                nextElement = withTimeoutOrNull(50) { tickerChannel.receive() } // 所有随后到来的元素都经过了100浩渺的延迟
                println("Next element is not ready in 50 ms: $nextElement")

                nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
                println("Next element is ready in 100 ms: $nextElement")

                // 模拟大量消费延迟
                println("Consumer pauses for 150ms")
                delay(150)
                // 下一个元素立即可用
                nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
                println("Next element is available immediately after large consumer delay: $nextElement")
                // 请注意，`receive` 调用之间的暂停被考虑在内，下一个元素的到达速度更快
                nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
                println("Next element is ready in 50ms after consumer pause in 150ms: $nextElement")

                tickerChannel.cancel() // 表明不再需要更多的元素
        }

        @Test
        fun test28() = runBlocking {
                val jobs = List(1_000_000) {
                        launch {
                                delay(10L)
                                println(it)
                        }
                }
                jobs.forEach { it.join() }
        }

}