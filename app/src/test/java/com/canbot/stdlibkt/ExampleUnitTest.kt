package com.canbot.stdlibkt

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

        var x = 0
        @Test
        fun main() {
                //函数引用
                val args = listOf(12, 23, 34, 25)
                args.filter(::isOld)
                        .map { "$it " }
                        .forEach(::print)
                println()
                //属性引用
                println(::x.get())
                ::x.set(2)
                println(x)
        }

        private fun isOld(age: Int): Boolean = age > 20
}
