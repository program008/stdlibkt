package com.canbot.coroutinekt

import kotlinx.coroutines.*
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
        @Test
        fun addition_isCorrect() {
                assertEquals(4, 2 + 2)
        }

        @Test
        fun fun1() {
                GlobalScope.launch {
                        delay(1000)
                        println("launch")
                }
                runBlocking {
                        print("delay")
                        delay(2000)
                }
        }

        @Test
        fun testMySuspendingFunction() = runBlocking<Unit> {
                // 这里我们可以使用挂起函数来实现任何我们喜欢的断言风格
                GlobalScope.launch {
                        worlk()
                }
                print("main")
                delay(2000)
        }

        @Test
        fun fun2() = runBlocking {
                val job = GlobalScope.launch {
                        delay(1000)
                        print("launch")
                }
                print("main")
                job.join()
        }

        @Test
        fun fun3() = runBlocking {
                launch {
                        worlk()
                }
                print("main")
        }
        //提取函数重构
        private suspend fun worlk() {
                delay(1000)
                print("launch")
        }

        //作用域构建器
        @Test
        fun fun4() = runBlocking { // this: CoroutineScope
                launch {
                        delay(200L)
                        println("Task from runBlocking")
                }

                coroutineScope { // 创建一个新的协程作用域
                        launch {
                                delay(500L)
                                println("Task from nested launch")
                        }

                        delay(100L)
                        println("Task from coroutine scope") // 该行将在嵌套启动之前执行打印
                }

                println("Coroutine scope is over") // 该行将在嵌套结束之后才会被打印
        }

        @Test
        fun fun5() = runBlocking {
                repeat(100_000){
                        launch {
                                delay(1000)
                                print(".")
                        }
                }
        }

        @Test
        fun fun6() = runBlocking {
                val job = launch {
                        repeat(1000) { i ->
                                println("I'm sleeping $i ...")
                                delay(500L)
                        }
                }
                delay(1300L) // 延迟一段时间
                println("main: I'm tired of waiting!")
                job.cancel() // 取消该任务
                job.join() // 等待任务执行结束
                println("main: Now I can quit.")
        }

}
