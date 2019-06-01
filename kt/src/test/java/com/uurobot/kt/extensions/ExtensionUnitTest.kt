package com.uurobot.kt.extensions

import com.uurobot.kt.MyClass
import org.junit.Test

/**
 *
 * Created by tao.liu on 2019/6/1.
 */
class ExtensionUnitTest {
        /**
         * 扩展函数
         */
        @Test
        fun test(){
                val list = mutableListOf(1,2,3)
                list.swap(0,2)
                println(list)
        }

        @Test
        fun test1(){
                val listOf = listOf(1, 2, 3, 4, 5)
                listOf.lastIndex.also(::println)
        }

        @Test
        fun test2(){
                MyClass.foo().also(::println)
        }
}