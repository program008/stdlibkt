package com.canbot.coroutinekt

import org.junit.Test

/**
 * Created by tao.liu on 2018/11/20.
 * description this is description
 */
class UnitTest {
        @Test
        fun test1(){
                durationFormat(94).also(::println)

        }

        /**
         * 时长格式化
         * 给定一个毫秒数，转换成时间格式
         */
        fun durationFormat(time:Long):String{
                val s = time%60
                val m = time/60
                return "$m:$s"
        }
}