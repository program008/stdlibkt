package com.uurobot.kt.inline

import org.junit.Test

/**
 * Created by tao.liu on 2019/8/26.
 * description this is description
 */

//当一个 inline 函数中，有多个 lambda 作为参数时，可以在不想内联的 lambda 前使用 noinline 声明
@Suppress("NOTHING_TO_INLINE")
inline fun sum(a: Int, b: Int, noinline lambda: (result: Int) -> Unit): Int {
        val r = a + b
        lambda.invoke(r)
        return r
}

/**
 * crossinline
 * 声明一个 lambda 不能有 return 语句(可以有 return@label 语句)。
 * 这样可以避免使用 inline 时，lambda 中的 return 影响程序流程
 */
inline fun sum2(a: Int, b: Int, crossinline lambda: (result: Int) -> Unit): Int {
        val r = a + b
        lambda.invoke(r)
        return r
}

@Test
fun main() {
        println("Start")
        sum(1, 2) {
                println("Result is: $it")
                //return // 这个会导致 main 函数 return
        }
        println("Done")

        println("Start2")
        sum2(3, 4) {
                println("Result2 is: $it")
                //return // 编译错误: return is not allowed here
        }
        println("Done2")
}
