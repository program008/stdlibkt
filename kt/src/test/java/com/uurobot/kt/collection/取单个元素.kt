package com.uurobot.kt.collection

import org.junit.Test

/**
 * Created by tao.liu on 2019/6/10.
 * description this is description
 */

class RetrieveSingle{
        @Test
        fun test1() {
                val numbers = linkedSetOf("one", "two", "three", "four", "five")
                println(numbers.elementAt(3))

                val numbersSortedSet = sortedSetOf("one", "two", "three", "four")
                println(numbersSortedSet.elementAt(0)) // elements are stored in the ascending order
        }

        @Test
        fun test2(){
                val numbers = listOf("one", "two", "three", "four", "five")
                println(numbers.first())
                println(numbers.last())
        }

        @Test
        fun test3() {
                val numbers = listOf("one", "two", "three", "four", "five")
                println(numbers.elementAtOrNull(5))
                println(numbers.elementAtOrElse(5) { index -> "The value for index $index is undefined"})
        }

        @Test
        fun test4(){
                val numbers = listOf("one", "two", "three", "four", "five", "six")
                println(numbers.first { it.length > 3 })
                println(numbers.last { it.startsWith("f") })
        }

        @Test
        fun test5(){
                val numbers = listOf("one", "two", "three", "four", "five", "six")
                println(numbers.firstOrNull { it.length > 6 })
        }

        @Test
        fun test6(){
                val numbers = listOf(1, 2, 3, 4)
                println(numbers.find { it % 2 == 0 })
                println(numbers.findLast { it % 2 == 0 })
        }

        @Test
        fun test7(){
                val numbers = listOf(1, 2, 3, 4)
                println(numbers.random())
        }

        @Test
        fun test8(){
                val numbers = listOf("one", "two", "three", "four", "five", "six")
                println(numbers.contains("four"))
                println("zero" in numbers)

                println(numbers.containsAll(listOf("four", "two")))
                println(numbers.containsAll(listOf("one", "zero")))
        }

        @Test
        fun test9(){
                val numbers = listOf("one", "two", "three", "four", "five", "six")
                println(numbers.isEmpty())
                println(numbers.isNotEmpty())

                val empty = emptyList<String>()
                println(empty.isEmpty())
                println(empty.isNotEmpty())
        }
}