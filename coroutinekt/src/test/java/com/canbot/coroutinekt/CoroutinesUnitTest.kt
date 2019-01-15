package com.canbot.coroutinekt

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.junit.Test
import java.io.IOException
import java.util.ArrayList
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random
import kotlin.system.measureTimeMillis

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

        // 组合挂起函数
        /**
         * 默认顺序调用
         */
        @Test
        fun test29() = runBlocking<Unit> {
                val time = measureTimeMillis {
                        val one = doSomethingUsefulOne()
                        val two = doSomethingUsefulTwo()
                        println("The answer is ${one + two}")
                }
                println("Completed in $time ms")
        }

        suspend fun doSomethingUsefulOne(): Int {
                delay(1000L) // 假设我们在这里做了些有用的事
                return 13
        }

        suspend fun doSomethingUsefulTwo(): Int {
                delay(1000L) // 假设我们在这里也做了一些有用的事
                return 29
        }

        /**
         * 使用async并发
         */
        @Test
        fun test30() = runBlocking<Unit> {
                val time = measureTimeMillis {
                        val one = async { doSomethingUsefulOne() }
                        val two = async { doSomethingUsefulTwo() }
                        println("The answer is ${one.await() + two.await()}")
                }
                println("Completed in $time ms")
        }

        /**
         * 惰性启动的async
         */
        @Test
        fun test31() = runBlocking<Unit> {
                val time = measureTimeMillis {
                        val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
                        val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
                        // 执行一些计算
                        one.start() // 启动第一个
                        two.start() // 启动第二个
                        println("The answer is ${one.await() + two.await()}")
                }
                println("Completed in $time ms")
        }

        /**
         * 使用 async 的结构化并发
         * 这种情况下，如果在 concurrentSum 函数内部发生了错误，并且它抛出了一个异常，
         * 所有在作用域中启动的协程都将会被取消
         */
        @Test
        fun test32() = runBlocking<Unit> {
                val time = measureTimeMillis {
                        println("The answer is ${concurrentSum()}")
                }
                println("Completed in $time ms")
        }

        suspend fun concurrentSum(): Int = coroutineScope {
                val one = async { doSomethingUsefulOne() }
                val two = async { doSomethingUsefulTwo() }
                one.await() + two.await()
        }

        /**
         * async 风格的函数
         * 不推荐使用
         *
         */
        @Test
        fun test33() {// 注意，在这个示例中我们在 `test33` 函数的右边没有加上 `runBlocking`
                val time = measureTimeMillis {
                        // 我们可以在协程外面启动异步执行
                        val one = somethingUsefulOneAsync()
                        val two = somethingUsefulTwoAsync()
                        // 但是等待结果必须调用其它的挂起或者阻塞
                        // 当我们等待结果的时候，这里我们使用 `runBlocking { …… }` 来阻塞主线程
                        runBlocking {
                                println("The answer is ${one.await() + two.await()}")
                        }
                }
                println("Completed in $time ms")
        }

        fun somethingUsefulOneAsync() = GlobalScope.async {
                /* val i = 1/0
                 repeat(1000){
                         delay(100L)
                         print(it)
                 }*/
                doSomethingUsefulOne()
        }

        fun somethingUsefulTwoAsync() = GlobalScope.async {
                doSomethingUsefulTwo()
        }

        /**
         * 取消始终通过协程的层次结构来进行传递：
         * 当第一个子协程失败的时候第一个 async 是如何等待父线程被取消的：
         */
        @Test
        fun test34() = runBlocking<Unit> {
                try {
                        failedConcurrentSum()
                } catch (e: ArithmeticException) {
                        println("Computation failed with ArithmeticException")
                }
        }

        suspend fun failedConcurrentSum(): Int = coroutineScope {
                val one = async<Int> {
                        try {
                                delay(Long.MAX_VALUE) // 模拟一个长时间的运算
                                42
                        } finally {
                                println("First child was cancelled")
                        }
                }
                val two = async<Int> {
                        println("Second child throws an exception")
                        throw ArithmeticException()
                }
                one.await() + two.await()
        }

        //----------------------------协程上下文与调度器-----------------------------------------
        /**
         * 调度器和线程
         */
        @Test
        fun test35() = runBlocking<Unit> {
                launch {
                        // 运行在父协程的上下文中，即 runBlocking 主协程
                        println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
                }
                launch(Dispatchers.Unconfined) {
                        // 不受限的——将工作在主线程中
                        println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
                }
                launch(Dispatchers.Default) {
                        // 将会获取默认调度器
                        println("Default               : I'm working in thread ${Thread.currentThread().name}")
                }
                launch(newSingleThreadContext("MyOwnThread")) {
                        // 将使它获得一个新的线程
                        println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
                }
        }

        /**
         * 非受限调度器VS受限调度器
         */
        @Test
        fun test36() = runBlocking<Unit> {
                launch(Dispatchers.Unconfined) {
                        // 非受限的——将和主线程一起工作
                        println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
                        delay(500)
                        println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
                }
                launch {
                        // 父协程的上下文，主 runBlocking 协程
                        println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
                        delay(1000)
                        println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
                }
        }

        fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")
        /**
         * 调试协程与线程
         */
        @Test
        fun test37() = runBlocking<Unit> {
                var timeMillis = measureTimeMillis {
                        val a = async {
                                log("I'm computing a piece of the answer")
                                6
                        }
                        val b = async {
                                log("I'm computing another piece of the answer")
                                7
                        }

                        log("The answer is ${a.await() * b.await()}")
                }
                println("timeMillis $timeMillis")

        }

        /**
         * 在不同的线程间跳转
         */
        @Test
        fun test38() {
                newSingleThreadContext("Ctx1").use { ctx1 ->
                        newSingleThreadContext("Ctx2").use { ctx2 ->
                                runBlocking(ctx1) {
                                        log("Started in ctx1")
                                        withContext(ctx2) {
                                                log("Working in ctx2")
                                        }
                                        log("Back to ctx1")
                                }
                        }
                }
        }

        /**
         * 子协程
         * 当一个协程被其它协程在 CoroutineScope 中启动的时候， 它将通过 CoroutineScope.coroutineContext
         * 来承袭上下文，并且这个新协程的 Job 将会成为父协程任务的 子 任务。当一个父协程被取消的时候，所有它的子协程也会被递归的取消。
         * 然而，当 GlobalScope 被用来启动一个协程时，它与作用域无关且是独立被启动的。
         */
        @Test
        fun test39() = runBlocking<Unit> {
                // 启动一个协程来处理某种传入请求（request）
                val request = launch {
                        // 孵化了两个子任务, 其中一个通过 GlobalScope 启动
                        GlobalScope.launch {
                                println("job1: I run in GlobalScope and execute independently!")
                                delay(1000)
                                println("job1: I am not affected by cancellation of the request")
                        }
                        // 另一个则承袭了父协程的上下文
                        launch {
                                delay(100)
                                println("job2: I am a child of the request coroutine")
                                delay(1000)
                                println("job2: I will not execute this line if my parent request is cancelled")
                        }
                }
                delay(500)
                request.cancel() // 取消请求（request）的执行
                delay(1000) // 延迟一秒钟来看看发生了什么
                println("main: Who has survived request cancellation?")
        }

        /**
         * 父协程的职责
         * 一个父协程总是等待所有的子协程执行结束。
         * 父协程并不显式的跟踪所有子协程的启动以及不必使用 Job.join 在最后的时候等待它们：
         */
        @Test
        fun test40() = runBlocking<Unit> {
                // 启动一个协程来处理某种传入请求（request）
                val request = launch {
                        repeat(3) { i ->
                                // 启动少量的子任务
                                launch {
                                        delay((i + 1) * 200L) // 延迟200毫秒、400毫秒、600毫秒的时间
                                        println("Coroutine $i is done")
                                }
                        }
                        println("request: I'm done and I don't explicitly join my children that are still active")
                }
                request.join() // 等待请求的完成，包括其所有子协程
                println("Now processing of the request is complete")
        }

        /**
         * 命名协程以用于调试
         */
        @Test
        fun test41() = runBlocking(CoroutineName("main")) {
                log("Started main coroutine")
                // 运行两个后台值计算
                val v1 = async(CoroutineName("v1coroutine")) {
                        delay(500)
                        log("Computing v1")
                        252
                }
                val v2 = async(CoroutineName("v2coroutine")) {
                        delay(1000)
                        log("Computing v2")
                        6
                }
                log("The answer for v1 / v2 = ${v1.await() / v2.await()}")
                /**
                 * 组合上下文中的元素有时我们需要在协程上下文中定义多个元素。
                 * 我们可以使用 + 操作符来实现。 比如说，我们可以显式指定一个调度器来启动协程并且同时显式指定一个命名：
                 */
                var launch = launch(Dispatchers.Default + CoroutineName("test")) {
                        println("I'm working in thread ${Thread.currentThread().name}")
                }
                println()
        }

        val threadLocal = ThreadLocal<String?>() // 声明线程局部变量
        /**
         * 线程局部数据
         */
        @Test
        fun test42() = runBlocking<Unit> {
                threadLocal.set("main")
                println("Pre-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
                val job = launch(Dispatchers.Default + threadLocal.asContextElement(value = "launch")) {
                        println("Launch start, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
                        yield()
                        println("After yield, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
                }
                job.join()
                println("Post-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        }

        //-----------------------------异常处理---------------------------------------------
        /**
         * 异常的传播
         */
        @Test
        fun test43() = runBlocking {
                val job = GlobalScope.launch {
                        println("Throwing exception from launch")
                        throw IndexOutOfBoundsException() // 我们将在控制台打印 Thread.defaultUncaughtExceptionHandler
                }
                job.join()
                println("Joined failed job")
                val deferred = GlobalScope.async {
                        println("Throwing exception from async")
                        throw ArithmeticException() // 没有打印任何东西，依赖用户去调用等待
                }
                try {
                        deferred.await()
                        println("Unreached")
                } catch (e: ArithmeticException) {
                        println("Caught ArithmeticException")
                }
        }

        @Test
        fun test44() = runBlocking {
                val handler = CoroutineExceptionHandler { _, exception ->
                        println("Caught $exception")
                }
                val job = GlobalScope.launch(handler) {
                        throw AssertionError()
                }
                val deferred = GlobalScope.async(handler) {
                        throw ArithmeticException() // 没有打印任何东西，依赖用户去调用 deferred.await()
                }
                joinAll(job, deferred)
        }

        /**
         * 取消与异常
         */
        @Test
        fun test45() = runBlocking {
                val job = launch {
                        val child = launch {
                                try {
                                        delay(Long.MAX_VALUE)
                                } finally {
                                        println("Child is cancelled")
                                }
                        }
                        yield()
                        println("Cancelling child")
                        child.cancel()
                        child.join()
                        yield()
                        println("Parent is not cancelled")
                }
                job.join()
        }

        @Test
        fun test46() = runBlocking {
                val handler = CoroutineExceptionHandler { _, exception ->
                        println("Caught $exception")
                }
                val job = GlobalScope.launch(handler) {
                        launch {
                                // 第一个子协程
                                try {
                                        delay(Long.MAX_VALUE)
                                } finally {
                                        withContext(NonCancellable) {
                                                println("Children are cancelled, but exception is not handled until all children terminate")
                                                delay(100)
                                                println("The first child finished its non cancellable block")
                                        }
                                }
                        }
                        launch {
                                // 第二个子协程
                                delay(10)
                                println("Second child throws an exception")
                                throw ArithmeticException()
                        }
                }
                job.join()
        }

        /**
         * 异常聚合
         */
        @Test
        fun test47() = runBlocking {
                val handler = CoroutineExceptionHandler { _, exception ->
                        println("Caught $exception with suppressed ${exception.suppressed.contentToString()}")
                }
                val job = GlobalScope.launch(handler) {
                        launch {
                                try {
                                        delay(Long.MAX_VALUE)
                                } finally {
                                        throw ArithmeticException()
                                }
                        }
                        launch {
                                delay(100)
                                throw IOException()
                        }
                        delay(Long.MAX_VALUE)
                }
                job.join()
        }

        /**
         * 取消异常是透明的并且会在默认情况下解包
         */
        @Test
        fun test48() = runBlocking {
                val handler = CoroutineExceptionHandler { _, exception ->
                        println("Caught original $exception")
                }
                val job = GlobalScope.launch(handler) {
                        val inner = launch {
                                launch {
                                        launch {
                                                throw IOException()
                                        }
                                }
                        }
                        try {
                                inner.join()
                        } catch (e: CancellationException) {
                                println("Rethrowing CancellationException with original cause")
                                throw e
                        }
                }
                job.join()
        }

        /**
         * 监督任务
         */
        @Test
        fun test49() = runBlocking {
                val supervisor = SupervisorJob()
                with(CoroutineScope(coroutineContext + supervisor)) {
                        // 启动第一个子任务--这个示例将会忽略它的异常（不要在实践中这么做！）
                        val firstChild = launch(CoroutineExceptionHandler { _, _ ->  }) {
                                println("First child is failing")
                                throw AssertionError("First child is cancelled")
                        }
                        // 启动第二个子任务
                        val secondChild = launch {
                                firstChild.join()
                                // 取消了第一个子任务且没有传播给第二个子任务
                                println("First child is cancelled: ${firstChild.isCancelled}, but second one is still active")
                                try {
                                        delay(Long.MAX_VALUE)
                                } finally {
                                        // 但是取消了监督的传播
                                        println("Second child is cancelled because supervisor is cancelled")
                                }
                        }
                        // 等待直到第一个子任务失败且执行完成
                        firstChild.join()
                        println("Cancelling supervisor")
                        supervisor.cancel()
                        secondChild.join()
                }
        }

        /**
         * 监督作用域
         * 对于作用域的并发，supervisorScope 可以被用来替代 coroutineScope 来实现相同的目的。
         * 它只会单向的传播并且当子任务自身执行失败的时候将它们全部取消。它也会在所有的子任务执行结束前等待，
         * 就像 coroutineScope 所做的那样。
         */
        @Test
        fun test50() = runBlocking {
                try {
                        supervisorScope {
                                val child = launch {
                                        try {
                                                println("Child is sleeping")
                                                delay(Long.MAX_VALUE)
                                        } finally {
                                                println("Child is cancelled")
                                        }
                                }
                                // 使用 yield 来给我们的子任务一个机会来执行打印
                                yield()
                                println("Throwing exception from scope")
                                throw AssertionError()
                        }
                } catch(e: AssertionError) {
                        println("Caught assertion error")
                }
        }

        /**
         * 监督协程中的异常
         * 常规的任务和监督任务之间的另一个重要区别是异常处理。
         * 每一个子任务应该通过异常处理机制处理自身的异常。
         * 这种差异来自于子任务的执行失败不会传播给它的父任务的事实。
         */
        @Test
        fun test51() = runBlocking {
                val handler = CoroutineExceptionHandler { _, exception ->
                        println("Caught $exception")
                }
                supervisorScope {
                        val child = launch(handler) {
                                println("Child throws an exception")
                                throw AssertionError()
                        }
                        println("Scope is completing")
                }
                println("Scope is completed")
        }

        //------------------------------select表达式（实验性的）----------------------------------------------
        fun CoroutineScope.fizz() = produce<String> {
                while (true) { // 每300毫秒发送一个 "Fizz"
                        delay(300)
                        send("Fizz")
                }
        }

        fun CoroutineScope.buzz() = produce<String> {
                while (true) { // 每500毫秒发送一个 "Buzz!"
                        delay(500)
                        send("Buzz!")
                }
        }

        suspend fun selectFizzBuzz(fizz: ReceiveChannel<String>, buzz: ReceiveChannel<String>) {
                select<Unit> { // <Unit> 意味着该 select 表达式不返回任何结果
                        fizz.onReceive { value ->  // 这是第一个 select 子句
                                println("fizz -> '$value'")
                        }
                        buzz.onReceive { value ->  // 这是第二个 select 子句
                                println("buzz -> '$value'")
                        }
                }
        }
        /**
         * 在通道中select
         */
        @Test
        fun test52()  = runBlocking<Unit> {
                val fizz = fizz()
                val buzz = buzz()
                repeat(7) {
                        selectFizzBuzz(fizz, buzz)
                }
                coroutineContext.cancelChildren() // 取消 fizz 和 buzz 协程
        }
        suspend fun selectAorB(a: ReceiveChannel<String>, b: ReceiveChannel<String>): String =
                select<String> {
                        a.onReceiveOrNull { value ->
                                if (value == null)
                                        "Channel 'a' is closed"
                                else
                                        "a -> '$value'"
                        }
                        b.onReceiveOrNull { value ->
                                if (value == null)
                                        "Channel 'b' is closed"
                                else
                                        "b -> '$value'"
                        }
                }

        /**
         * 通道关闭时select
         */
        @Test
        fun test53() = runBlocking<Unit> {
                val a = produce<String> {
                        repeat(4) { send("Hello $it") }
                }
                val b = produce<String> {
                        repeat(4) { send("World $it") }
                }
                repeat(8) { // 打印最早的八个结果
                        println(selectAorB(a, b))
                }
                coroutineContext.cancelChildren()
        }

        fun CoroutineScope.produceNumbers(side: SendChannel<Int>) = produce<Int> {
                for (num in 1..10) { // 生产从 1 到 10 的10个数值
                        delay(100) // 延迟100毫秒
                        select<Unit> {
                                onSend(num) {} // 发送到主通道
                                side.onSend(num) {} // 或者发送到 side 通道
                        }
                }
        }

        /**
         * Select 以发送
         * Select 表达式具有 onSend 子句，可以很好的与选择的偏向特性结合使用。
         */
        @Test
        fun test54() = runBlocking<Unit> {
                val side = Channel<Int>() // 分配 side 通道
                launch { // 对于 side 通道来说，这是一个很快的消费者
                        side.consumeEach { println("Side channel has $it") }
                }
                produceNumbers(side).consumeEach {
                        println("Consuming $it")
                        delay(250) // 不要着急，让我们正确消化消耗被发送来的数字
                }
                println("Done consuming")
                coroutineContext.cancelChildren()
        }

        fun CoroutineScope.asyncString(time: Int) = async {
                delay(time.toLong())
                "Waited for $time ms"
        }

        fun CoroutineScope.asyncStringsList(): List<Deferred<String>> {
                val random = Random(3)
                return List(12) { asyncString(random.nextInt(1000)) }
        }

        /**
         * Select 延迟值
         * 延迟值可以使用 onAwait 子句查询。
         */
        @Test
        fun test55() = runBlocking<Unit> {
                val list = asyncStringsList()
                val result = select<String> {
                        list.withIndex().forEach { (index, deferred) ->
                                deferred.onAwait { answer ->
                                        "Deferred $index produced answer '$answer'"
                                }
                        }
                }
                println(result)
                val countActive = list.count { it.isActive }
                println("$countActive coroutines are still active")
        }
        fun CoroutineScope.switchMapDeferreds(input: ReceiveChannel<Deferred<String>>) = produce<String> {
                var current = input.receive() // 从第一个接收到的延迟值开始
                while (isActive) { // 循环直到被取消或关闭
                        val next = select<Deferred<String>?> { // 从这个 select 中返回下一个延迟值或 null
                                input.onReceiveOrNull { update ->
                                        update // 替换下一个要等待的值
                                }
                                current.onAwait { value ->
                                        send(value) // 发送当前延迟生成的值
                                        input.receiveOrNull() // 然后使用从输入通道得到的下一个延迟值
                                }
                        }
                        if (next == null) {
                                println("Channel was closed")
                                break // 跳出循环
                        } else {
                                current = next
                        }
                }
        }

        fun CoroutineScope.asyncString(str: String, time: Long) = async {
                delay(time)
                str
        }

        /**
         * 在延迟值通道上切换
         * 我们现在来编写一个通道生产者函数，它消费一个产生延迟字符串的通道，并等待每个接收的延迟值，
         * 但它只在下一个延迟值到达或者通道关闭之前处于运行状态。此示例将 onReceiveOrNull 和 onAwait 子句放在同一个 select 中：
         */
        @Test
        fun test56() = runBlocking<Unit> {
                val chan = Channel<Deferred<String>>() // 测试使用的通道
                launch { // 启动打印协程
                        for (s in switchMapDeferreds(chan))
                                println(s) // 打印每个获得的字符串
                }
                chan.send(asyncString("BEGIN", 100))
                delay(200) // 充足的时间来生产 "BEGIN"
                chan.send(asyncString("Slow", 500))
                delay(100) // 不充足的时间来生产 "Slow"
                chan.send(asyncString("Replace", 100))
                delay(500) // 在最后一个前给它一点时间
                chan.send(asyncString("END", 500))
                delay(1000) // 给执行一段时间
                chan.close() // 关闭通道……
                delay(500) // 然后等待一段时间来让它结束
        }

        //---------------------------共享的可变状态与并发--------------------------------

        suspend fun CoroutineScope.massiveRun(action: suspend () -> Unit) {
                val n = 100  // 启动的协程数量
                val k = 1000 // 每个协程重复执行同一动作的次数
                val time = measureTimeMillis {
                        val jobs = List(n) {
                                launch {
                                        repeat(k) { action() }
                                }
                        }
                        jobs.forEach { it.join() }
                }
                println("Completed ${n * k} actions in $time ms")
        }
        //@Volatile 无济于事
        var counter = 0
        @Test
        fun test_01()  = runBlocking<Unit> {
                GlobalScope.massiveRun {
                        counter++
                }
                println("Counter = $counter")
        }

        val mtContext = newFixedThreadPoolContext(2, "mtPool") // 明确地用两个线程自定义上下文
        @Test
        fun test_02() = runBlocking<Unit> {
                CoroutineScope(mtContext).massiveRun { // 在此及以下示例中使用刚才定义的上下文，而不是默认的 Dispatchers.Default
                        counter++
                }
                println("Counter = $counter")
        }

        var counter2 = AtomicInteger()
        /**
         * 线程安全的数据结构
         */
        @Test
        fun test_03() = runBlocking<Unit> {
                GlobalScope.massiveRun {
                        counter2.incrementAndGet()
                }
                println("Counter = ${counter2.get()}")
        }

        /**
         * 以细粒度限制线程
         * 限制线程 是解决共享可变状态问题的一种方案：对特定共享状态的所有访问权都限制在单个线程中。
         * 它通常应用于 UI 程序中：所有 UI 状态都局限于单个事件分发线程或应用主线程中。这在协程中很容易实现，通过使用一个单线程上下文：
         * 这段代码运行非常缓慢，因为它进行了 细粒度 的线程限制。每个增量操作都得使用 withContext 块从多线程
         * Dispatchers.Default 上下文切换到单线程上下文。
         */
        val counterContext = newSingleThreadContext("CounterContext")
        @Test
        fun test_04()  = runBlocking<Unit> {
                GlobalScope.massiveRun { // 使用 DefaultDispathcer 运行每个协程
                        withContext(counterContext) { // 但是把每个递增操作都限制在此单线程上下文中
                                counter++
                        }
                }
                println("Counter = $counter")
        }

        /**
         * 以粗粒度限制线程
         * 在实践中，线程限制是在大段代码中执行的，例如：状态更新类业务逻辑中大部分都是限于单线程中。
         * 下面的示例演示了这种情况， 在单线程上下文中运行每个协程。 这里我们使用 CoroutineScope() 函数来切换协程上下文为 CoroutineScope：
         * 这段代码运行更快而且打印出了正确的结果。
         */
        @Test
        fun test_05() = runBlocking<Unit> {
                CoroutineScope(counterContext).massiveRun { // 在单线程上下文中运行每个协程
                        counter++
                }
                println("Counter = $counter")
        }

        /**
         * 互斥
         * 该问题的互斥解决方案：使用永远不会同时执行的 关键代码块 来保护共享状态的所有修改。
         * 在阻塞的世界中，你通常会为此目的使用 synchronized 或者 ReentrantLock。 在协程中的替代品叫做 Mutex 。
         * 它具有 lock 和 unlock 方法， 可以隔离关键的部分。关键的区别在于 Mutex.lock() 是一个挂起函数，它不会阻塞线程。
         * 还有 withLock 扩展函数，可以方便的替代常用的 mutex.lock(); try { …… } finally { mutex.unlock() } 模式：
         *
         * 此示例中锁是细粒度的，因此会付出一些代价。但是对于某些必须定期修改共享状态的场景，
         * 它是一个不错的选择，但是没有自然线程可以限制此状态。
         */
        val mutex = Mutex()
        @Test
        fun test_06() = runBlocking<Unit> {
                GlobalScope.massiveRun {
                        mutex.withLock {
                                counter++
                        }
                }
                println("Counter = $counter")
        }


        // 这个函数启动一个新的计数器 actor
        fun CoroutineScope.counterActor() = actor<CounterMsg> {
                var counter = 0 // actor 状态
                for (msg in channel) { // 即将到来消息的迭代器
                        when (msg) {
                                is IncCounter -> counter++
                                is GetCounter -> msg.response.complete(counter)
                        }
                }
        }

        /**
         * Actors
         * 一个 actor 是由协程、被限制并封装到该协程中的状态以及一个与其它协程通信的 通道 组合而成的一个实体。
         * 一个简单的 actor 可以简单的写成一个函数， 但是一个拥有复杂状态的 actor 更适合由类来表示。
         * 有一个 actor 协程构建器，它可以方便地将 actor 的邮箱通道组合到其作用域中（用来接收消息）、
         * 组合发送 channel 与结果集对象，这样对 actor 的单个引用就可以作为其句柄持有。
         * 使用 actor 的第一步是定义一个 actor 要处理的消息类。 Kotlin 的密封类很适合这种场景。
         * 我们使用 IncCounter 消息（用来递增计数器）和 GetCounter 消息（用来获取值）来定义 CounterMsg 密封类。
         * 后者需要发送回复。CompletableDeferred 通信原语表示未来可知（可传达）的单个值， 因该特征它被用于此处
         */
        @Test
        fun test_07() = runBlocking<Unit> {
                val counter = counterActor() // 创建该 actor
                GlobalScope.massiveRun {
                        counter.send(IncCounter)
                }
                // 发送一条消息以用来从一个 actor 中获取计数值
                val response = CompletableDeferred<Int>()
                counter.send(GetCounter(response))
                println("Counter = ${response.await()}")
                counter.close() // 关闭该actor
        }
        fun testtt(){
                GlobalScope.launch(Dispatchers.IO)  {
                        //Logger.e(TAG, "发送的命令$cmd")
                        //telnet.sendCommand(cmd)
                }
        }
        /**
         * actor 本身执行时所处上下文（就正确性而言）无关紧要。一个 actor 是一个协程，
         * 而一个协程是按顺序执行的，因此将状态限制到特定协程可以解决共享可变状态的问题。
         * 实际上，actor 可以修改自己的私有状态，但只能通过消息互相影响（避免任何锁定）。
         * actor 在高负载下比锁更有效，因为在这种情况下它总是有工作要做，而且根本不需要切换到不同的上下文。
         * 注意，actor 协程构建器是一个双重的 produce 协程构建器。一个 actor 与它接收消息的通道相关联，
         * 而一个 producer 与它发送元素的通道相关联。
         */
}