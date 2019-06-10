package com.uurobot.kt.collection

import org.junit.Test

/**
 * Created by tao.liu on 2019/6/10.
 * description this is description
 */
class FilterCollection {
        @Test
        fun test1() {
                val numbers = listOf("one", "two", "three", "four")
                val longerThan3 = numbers.filter { it.length > 3 }
                println(longerThan3)

                val numbersMap = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key11" to 11)
                val filteredMap = numbersMap.filter { (key, value) -> key.endsWith("1") && value > 10 }
                println(filteredMap)
        }

        @Test
        fun test2() {
                val numbers = listOf("one", "two", "three", "four")
                numbers.filterIndexed { index, value -> index > 1 }.also(::println)
        }

        @Test
        fun test3() {
                val numbers = listOf("one", "two", "three", "four")

                val filteredIdx = numbers.filterIndexed { index, s -> (index != 0) && (s.length < 5) }
                val filteredNot = numbers.filterNot { it.length <= 3 }

                println(filteredIdx)
                println(filteredNot)
        }

        @Test
        fun test4() {
                val numbers = listOf(null, 1, "two", 3.0, "four")
                println("All String elements in upper case:")
                numbers.filterIsInstance<String>().forEach {
                        println(it.toUpperCase())
                }
        }

        @Test
        fun test5(){
                val numbers = listOf(null, "one", "two", null)
                numbers.filterNotNull().forEach {
                        println(it.length)   // length is unavailable for nullable Strings
                }
        }

        @Test
        fun test6(){
                val numbers = listOf("one", "two", "three", "four")
                //划分 第一个参数是匹配到的数据，第二参数是剩余的参数
                val (match, rest) = numbers.partition { it.length > 3 }

                println(match)
                println(rest)
        }

        @Test
        fun test7(){
                val numbers = listOf("one", "two", "three", "four")

                println(numbers.any { it.endsWith("e") })
                println(numbers.none { it.endsWith("a") })
                println(numbers.all { it.endsWith("e") })

                println(emptyList<Int>().all { it > 5 })
        }

        @Test
        fun test8(){
                val numbers = listOf("one", "two", "three", "four")
                val empty = emptyList<String>()
                // 检测谓词也可以没有参数 没有带参数，表示过滤是否有数据
                println(numbers.any())
                println(empty.any())

                println(numbers.none())
                println(empty.none())
        }

}