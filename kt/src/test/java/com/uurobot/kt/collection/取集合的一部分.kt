package com.uurobot.kt.collection

import org.junit.Test

/**
 * Created by tao.liu on 2019/6/10.
 * description this is description
 */
class RetrieveCollection{

        @Test
        fun test1(){
                val numbers = listOf("one", "two", "three", "four", "five", "six")
                println(numbers.slice(1..3))
                println(numbers.slice(0..4 step 2))
                println(numbers.slice(setOf(3, 5, 0)))
        }

        @Test
        fun test2(){
                val numbers = listOf("one", "two", "three", "four", "five", "six")
                println(numbers.take(3))
                println(numbers.takeLast(3))
                println(numbers.drop(1))
                println(numbers.dropLast(5))
        }

        @Test
        fun test3(){
                val numbers = listOf("one", "two", "three", "four", "five", "six")
                println(numbers.takeWhile { !it.startsWith('f') })
                println(numbers.takeLastWhile { it != "three" })
                println(numbers.takeWhile { it != "three" })
                println(numbers.dropWhile { it.length == 3 })
                println(numbers.dropLastWhile { it.contains('i') })
        }

        @Test
        fun test4(){
                val numbers = (0..12).toList()
                println(numbers.chunked(3))
        }

        @Test
        fun test5(){
                val numbers = (0..13).toList()
                println(numbers.chunked(3) { it.sum() })  // `it` is a chunk of the original collection
        }

        @Test
        fun test6(){
                val numbers = listOf("one", "two", "three", "four", "five")
                println(numbers.windowed(3))
                println(numbers.chunked(3))
        }

        @Test
        fun test7(){
                val numbers = (1..10).toList()
                println(numbers.windowed(3, step = 2, partialWindows = true))
                println(numbers.windowed(3) { it.sum() })
        }

        @Test
        fun test8(){
                val numbers = listOf("one", "two", "three", "four", "five")
                println(numbers.zipWithNext())
                println(numbers.zipWithNext() { s1, s2 -> s1.length > s2.length})
        }
}