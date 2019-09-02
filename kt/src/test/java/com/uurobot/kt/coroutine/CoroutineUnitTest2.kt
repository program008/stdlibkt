package com.uurobot.kt.coroutine

import kotlinx.coroutines.*
import org.junit.Test

/**
 * Created by tao.liu on 2019/9/2.
 * 协程相关
 */
class CoroutineUnitTest2 {
        /**
         * runBlocking创建新的协程运行在当前线程上，所以会堵塞当前线程，直到协程体结束
         *
         * 适用范围: 用于启动一个协程任务，通常只用于启动最外层的协程，例如线程环境切换到协程环境。
         *
         * 打印出:
         * current thread = main,1
         * runBlocking thread = main @coroutine#1,1
         * runBlocking end
         * current thread end
         */
        @Test
        fun runBlockingTest() {
                println("current thread = ${Thread.currentThread().name},${Thread.currentThread().id}")
                //runBlocking运行在当前线程上，堵塞当前线程
                runBlocking {
                        println("runBlocking thread = ${Thread.currentThread().name},${Thread.currentThread().id}")
                        delay(2000)
                        println("runBlocking end")
                }
                //等待runBlocking协程体内的内容执行完毕，才继续执行
                println("current thread end")
        }

        /**
         * GlobalScope.launch默认运行在后台新的调度线程中，并且不堵塞当前线程
         *
         * 适用范围: 需要启动异步线程处理的情况
         *
         * 输出:
         * current thread = main, 1
         * current thread end
         * launch thread = DefaultDispatcher-worker-2 @coroutine#1, 12
         * launch thread end
         */
        @Test
        fun launchTest() {
                println("current thread = ${Thread.currentThread().name}, ${Thread.currentThread().id}")
                //launch启动后台调度线程，并且不堵塞当前线程
                GlobalScope.launch {
                        println("launch thread = ${Thread.currentThread().name}, ${Thread.currentThread().id}")
                        delay(1000)
                        println("launch thread end")
                }
                println("current thread end")

                //当前线程休眠以便调度线程有机会执行
                Thread.sleep(3000)
        }


        /**
         * async与launch的相同点:都是不堵塞当前线程并启动后台调度线程。
         * 区别: 1.async返回类型为Deferred, launch返回类型为job
         *       2.async可以在协程体中存在自定义的返回值，并且通过Deferred.await堵塞当前线程等待接收async协程返回的类型。
         *
         * 适用范围: 特别是需要启动异步线程处理并等待处理结果返回的场景
         *
         * 打印出:
         * current thread = main @coroutine#1, 1
         * current thread end
         * async thread = DefaultDispatcher-worker-1 @coroutine#2, 11
         * async end
         * result = 123
         */
        @Test
        fun asyncTest() {
                runBlocking {
                        println("current thread = ${Thread.currentThread().name}, ${Thread.currentThread().id}")
                        //launch启动后台调度线程，并且不堵塞当前线程
                        val deferred = GlobalScope.async {
                                println("async thread = ${Thread.currentThread().name}, ${Thread.currentThread().id}")
                                delay(1000)
                                println("async end")
                                //需要通过标签的方式返回
                                return@async "123"
                        }
                        println("current thread end")
                        val result = deferred.await()
                        println("result = $result")
                        //当前线程休眠以便调度线程有机会执行
                        Thread.sleep(3000)
                }
        }

        /*******************************************调度器**************************************************/
        /**
         * 缺省的调度器是空的，默认运行在当前线程上
         *
         * 打印出:
         *
         * runBlocking thread = Instr: android.support.test.runner.AndroidJUnitRunner,1397
         * runBlocking end
         * launch thread = Instr: android.support.test.runner.AndroidJUnitRunner,1397
         */
        @Test
        fun dispatcher() = runBlocking {
                println(" runBlocking thread = ${Thread.currentThread().name},${Thread.currentThread().id}")
                launch {
                        println(" launch thread = ${Thread.currentThread().name},${Thread.currentThread().id}")
                }
                println(" runBlocking end")
                delay(3000)
        }

        /**
         * 使用default调度器(注意和缺省的不一样),会另起新线程执行协程体内容
         *
         * 打印出:
         *
         * runBlocking thread = Instr: android.support.test.runner.AndroidJUnitRunner,1437
         * runBlocking end
         * default thread = DefaultDispatcher-worker-2,1440
         * runBlocking end2
         * default end
         */
        @Test
        fun dispatcherDefault() = runBlocking {
                println(" runBlocking thread = ${Thread.currentThread().name},${Thread.currentThread().id}")
                launch(Dispatchers.Default) {
                        println(" default thread = ${Thread.currentThread().name},${Thread.currentThread().id}")
                        //因为是新线程，所以sleep不影响runBlocking end2输出
                        Thread.sleep(10000)
                        println(" default end")
                }
                println(" runBlocking end")
                //不同线程，sleep不会导致launch也跟着等待
                Thread.sleep(3000)
                println(" runBlocking end2")
        }

        /**
         *
         * main调度器会在主线程执行协程体内容
         *
         *
         * 输出:
         *
         * runBlocking thread = Instr: android.support.test.runner.AndroidJUnitRunner,1457
         * runBlocking end
         * main thread = main,1
         */
        @Test
        fun dispatcherMain() = runBlocking {
                println(" runBlocking thread = ${Thread.currentThread().name},${Thread.currentThread().id}")
                launch(Dispatchers.Main) {
                        println(" main thread = ${Thread.currentThread().name},${Thread.currentThread().id}")
                }
                println(" runBlocking end")
        }


        /**
         * 使用io调度器,和default类似会另起新线程执行协程体内容，只是更适合于密集的io操作
         *
         * 打印出:
         *
         * runBlocking thread = Instr: android.support.test.runner.AndroidJUnitRunner,1477
         * runBlocking end
         * IO thread = DefaultDispatcher-worker-2,1480
         * runBlocking end2
         * IO end
         */
        @Test
        fun dispatcherIO() = runBlocking {
                println(" runBlocking thread = ${Thread.currentThread().name},${Thread.currentThread().id}")
                launch(Dispatchers.IO) {
                        println(" IO thread = ${Thread.currentThread().name},${Thread.currentThread().id}")
                        //因为是新线程，所以sleep不影响runBlocking end2输出
                        Thread.sleep(10000)
                        println(" IO end")
                }
                println(" runBlocking end")
                //不同线程，sleep不会导致launch也跟着等待
                Thread.sleep(3000)
                println(" runBlocking end2")
        }

        /**
         * 使用Unconfined调度器,会立即在当前线程执行协程体内容，是目前调度器中唯一会堵塞当前线程的
         *
         * 打印出:
         *
         * runBlocking thread = Instr: android.support.test.runner.AndroidJUnitRunner,1482
         * Unconfined thread = Instr: android.support.test.runner.AndroidJUnitRunner,1482
         * Unconfined end
         * runBlocking end
         * runBlocking end2
         * IO end
         */
        @Test
        fun dispatcherUnconfined() = runBlocking {
                println(" runBlocking thread = ${Thread.currentThread().name},${Thread.currentThread().id}")
                launch(Dispatchers.Unconfined) {
                        println(" Unconfined thread = ${Thread.currentThread().name},${Thread.currentThread().id}")
                        Thread.sleep(10000)
                        println(" Unconfined end")
                }
                //相同线程，需要等待Unconfined执行完毕才可以接着往下执行
                println(" runBlocking end")
                //
                Thread.sleep(3000)
                println(" runBlocking end2")
        }

        /**
         * 启动模式, 缺省时
         *
         * 协程立即等待被调度执行(等待被调度，不是立即执行)
         *
         * 打印:
         * 1
         * 3
         * 2
         */
        @Test
        fun coroutineStart() = runBlocking {
                println("1")
                launch {
                        println("2")
                }
                println("3")
                delay(3000)
        }

        /**
         * DEFAULT启动模式，效果同缺省时一致
         */
        @Test
        fun default() = runBlocking {
                println("1")
                launch(start = CoroutineStart.DEFAULT) {
                        println("2")
                }
                println("3")
                delay(3000)
        }

        /**
         * UNDISPATCHED启动模式，立即运行该协程体内容(相比其它启动方式少了等待过程)
         *
         * 打印:
         * 1
         * 2
         * 3
         */
        @ExperimentalCoroutinesApi
        @Test
        fun unDispatched() = runBlocking {
                println("1")
                launch(start = CoroutineStart.UNDISPATCHED) {
                        //2优先于3执行
                        println("2")
                }
                println("3")
        }

        /**
         * lazy启动方式，需要主动触发才能进入等待调度阶段
         *
         * - 调用 Job.start，主动触发协程的调度执行
         * - 调用 Job.join，隐式的触发协程的调度执行
         *
         * 使用start触发打印出:
         * 1
         * 3
         * 2
         * 使用join触发打印出:
         * 1
         * 2
         * 3
         * 如果下述例子去掉job.start(),则肯定是:
         * 1
         * 3
         */
        @Test
        fun lazy() = runBlocking {
                println("1")
                val job = launch(start = CoroutineStart.LAZY) {
                        //2优先于3执行
                        println("2")
                }

                //lazy需要手动触发，让其协程进入等待调度阶段
                job.start()
                //join会堵塞当前协程等待job协程执行完毕
                //job.join()
                println("3")
        }

        /**
         * 注意一旦lazy协程体没有通过start执行完毕或者通过cancel取消掉，则runBlocking永远不会退出。
         * runBlocking会等到里面全部协程结束才退出
         *
         * 打印出:
         * 1
         * 3
         * 4
         */
        @Test
        fun lazy2() = runBlocking {
                println("1")
                val job = launch(start = CoroutineStart.LAZY) {
                        println("2")
                }
                println("3")
                //job.start()
                //job.cancel()
                delay(3000)
                println("4")
        }


        /**
         * ATOMIC启动模式，立即等待被调度执行，并且开始执行前无法被取消，直到执行完毕或者遇到第一个挂起点。
         *
         * 该示例可以看出在同样经过cancel操作后，atomic协程依旧会被启动，而其它则不会启动了
         * 打印出:
         * 1
         * 3
         * atomic run
         */
        @Test
        fun atomic() = runBlocking {
                println("1")
                val job = launch(start = CoroutineStart.ATOMIC) {
                        println("atomic run")
                }
                job.cancel()
                println("3")
        }

        /**
         *
         * 该示例演示atomic被cancel后遇到第一个挂起点取消运行的效果
         *
         * 打印出:
         *
         * 1
         * 2
         * atomic run
         * 3
         */
        @Test
        fun atomic2() = runBlocking {
                println("1")
                val job = launch(start = CoroutineStart.ATOMIC) {
                        println("atomic run")
                        //遇到了挂起点,cancel生效，不会再执行打印atomic end
                        delay(3000)
                        println("atomic end")
                }
                job.cancel()
                println("2")
                delay(5000)
                println("3")
        }

        /**
         * 该例子可以看出，在未运行前，default,lazy可以被cancel取消,
         * unDidpatcher因为会立即在当前线程执行，所以该例子中的cancel本身没啥意义了
         *
         * 输出:
         * 1
         * unDispatcherJob run
         * 2
         * atomic run
         */
        @Test
        fun atomic3() = runBlocking {
                println("1")
                val job = launch(start = CoroutineStart.ATOMIC) {
                        println("atomic run")
                }
                job.cancel()

                val defaultJob = launch(start = CoroutineStart.DEFAULT) {
                        println("default run")
                }
                defaultJob.cancel()

                val lazyJob = launch(start = CoroutineStart.LAZY) {
                        println("lazyJob run")
                }
                lazyJob.start()
                lazyJob.cancel()

                val unDispatcherJob = launch(start = CoroutineStart.UNDISPATCHED) {
                        println("unDispatcherJob run")
                }
                unDispatcherJob.cancel()

                println("2")
        }

        /*******************************异常处理**************************************************/

        @Test
        fun globalScopeLaunch() {
                println("start")
                GlobalScope.launch {
                        println("launch Throwing exception")
                        throw NullPointerException()
                }
                Thread.sleep(3000)
                //GlobalScope.launch产生的异常不影响该线程执行
                println("end")
        }

        @Test
        fun globalScopeLaunch2() = runBlocking {
                println("start ${Thread.currentThread().id}")
                GlobalScope.launch {
                        println("launch Throwing exception ${Thread.currentThread().id}")
                        throw NullPointerException()
                }.join()

                GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
                        println("launch UNDISPATCHED Throwing exception ${Thread.currentThread().id}")
                        throw IndexOutOfBoundsException()
                }.join()

                //GlobalScope.launch产生的异常不影响该协程执行
                println("end ${Thread.currentThread().id}")
        }


        @Test
        fun globalScopeAsync() = runBlocking {
                println("start ${Thread.currentThread().id}")
                val deferred = GlobalScope.async {
                        println("async Throwing exception ${Thread.currentThread().id}")
                        throw NullPointerException()
                }
                //deferred.join()
                deferred.await()
                //join不会接收异常可以正常执行下面步骤,await会接收到抛出的异常导致后面步骤无法执行
                //不使用join和await则可以正常执行下面步骤
                println("end ${Thread.currentThread().id}")
                delay(3000)
        }

        @Test
        fun globalScopeAsync2() {
                println("start")
                val deferred = GlobalScope.async {
                        println("async Throwing exception")
                        throw NullPointerException()
                }
                Thread.sleep(3000)
                //非协程环境无法使用await,上边产生的异常不影响该线程执行
                println("end")
        }

        @Test
        fun launchChildException() = runBlocking {
                println("start ${Thread.currentThread().id}")
                //这边换成async也是一样结果
                launch {
                        println("launch Throwing exception ${Thread.currentThread().id}")
                        throw NullPointerException()
                }
                delay(3000)
                //因为子协程出现异常，默认会导致父协程终止，下边不会被执行
                println("end ${Thread.currentThread().id}")
        }

        @Test
        fun launchChildExceptionIO() = runBlocking {
                println("start ${Thread.currentThread().id}")
                //这边换成async也是一样结果
                launch(Dispatchers.IO) {
                        println("launch Throwing exception ${Thread.currentThread().id}")
                        throw NullPointerException()
                }
                delay(3000)
                //因为子协程出现异常，默认会导致父协程终止，下边不会被执行
                println("end ${Thread.currentThread().id}")
        }

        @Test
        fun asyncCatchException() = runBlocking {
                println("start ${Thread.currentThread().id}")
                val deferred = GlobalScope.async {
                        println("async Throwing exception ${Thread.currentThread().id}")
                        throw NullPointerException()
                }
                try {
                        //await能够被try catch捕获错误，从而继续玩下执行
                        deferred.await()
                        //join本身忽视错误，有没有try catch都一样
                        //deferred.join()
                } catch (exception: NullPointerException) {
                        println("catch exception")
                }
                println("end ${Thread.currentThread().id}")
                delay(3000)
        }

        @Test
        fun coroutineExceptionHandler() = runBlocking {

                val handleException = CoroutineExceptionHandler { _, throwable ->
                        //可以捕获到launch中抛出的异常,但是不能捕获async
                        println("CoroutineExceptionHandler catch $throwable")
                }
                println("start ${Thread.currentThread().id}")
                val job = GlobalScope.launch(handleException) {
                        println("launch Throwing exception ${Thread.currentThread().id}")
                        throw NullPointerException()
                }

                job.join()
                println("end ${Thread.currentThread().id}")
                delay(3000)
        }

        @Test
        fun asyncCoroutineExceptionHandler() = runBlocking {

                val handleException = CoroutineExceptionHandler { _, throwable ->
                        //可以捕获到launch中抛出的异常,但是不能捕获async
                        println("CoroutineExceptionHandler catch $throwable")
                }
                println("start ${Thread.currentThread().id}")
                val referred = GlobalScope.async(handleException) {
                        println("async Throwing exception ${Thread.currentThread().id}")
                        throw IndexOutOfBoundsException()
                }
                //  referred.join()
                 // referred.await()
                println("end ${Thread.currentThread().id}")
                delay(3000)
        }


        @Test
        fun launchCancelException()  = runBlocking {
                val handleException = CoroutineExceptionHandler { _, throwable ->
                        //无法捕获到launch中抛出的CancellationException异常
                        println("CoroutineExceptionHandler catch $throwable")
                }
                println("start ${Thread.currentThread().id}")
                launch(handleException) {
                        println("launch Throwing exception ${Thread.currentThread().id}")
                        //不会导致父协程终止
                        throw CancellationException()
                }
                delay(3000)
                //因为子协程出现异常，默认会导致父协程终止，但CancellationException不会
                println("end ${Thread.currentThread().id}")
        }

        @Test
        fun asyncCancelException()  = runBlocking {
                val handleException = CoroutineExceptionHandler { _, throwable ->
                        //无法捕获到launch中抛出的CancellationException异常
                        println("CoroutineExceptionHandler catch $throwable")
                }
                println("start ${Thread.currentThread().id}")
                val deferred = async(handleException) {
                        println("async Throwing exception ${Thread.currentThread().id}")
                        //不会导致父协程终止
                        throw CancellationException()
                }
                //导致CancellationException异常抛出到该线程，下边无法执行
                deferred.await()

                //若没有await则下边可以正常执行
                delay(3000)
                println("end ${Thread.currentThread().id}")
        }

}