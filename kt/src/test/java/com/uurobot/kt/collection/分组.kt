package com.uurobot.kt.collection

import org.junit.Test

/**
 * Created by tao.liu on 2019/6/10.
 * description this is description
 */
class Grouping{
        @Test
        fun test1() {
                val numbers = listOf("one", "two", "three", "four", "five")

                println(numbers.groupBy { it.first().toUpperCase() })
                println(numbers.groupBy(keySelector = { it.first().toUpperCase() }, valueTransform = { it.toUpperCase() }))
        }
        @Test
        fun test2() {
                val numbers = listOf("one", "two", "three", "four", "five", "six")
                println(numbers.groupingBy { it.first() }.eachCount())
        }
}