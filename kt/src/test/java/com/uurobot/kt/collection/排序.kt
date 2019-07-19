package com.uurobot.kt.collection

import org.junit.Test

/**
 * Created by tao.liu on 2019/6/14.
 * description this is description
 */

class CollectionOrdering {
        @Test
        fun test1() {
                println(Version(1, 2) > Version(1, 3))
                println(Version(2, 0) > Version(1, 5))
        }

        @Test
        fun test2() {
                val lengthComparator = Comparator { str1: String, str2: String -> str1.length - str2.length }
                println(listOf("aaa", "bb", "c").sortedWith(lengthComparator))

                println(listOf("aaa", "bb", "c").sortedWith(compareBy { it.length }))
        }

        @Test
        fun test3(){
                val numbers = listOf("one", "two", "three", "four")

                println("Sorted ascending: ${numbers.sorted()}")
                println("Sorted descending: ${numbers.sortedDescending()}")
        }

        @Test
        fun test4(){
                val numbers = listOf("one", "two", "three", "four")

                val sortedNumbers = numbers.sortedBy { it.length }
                println("Sorted by length ascending: $sortedNumbers")
                val sortedByLast = numbers.sortedByDescending { it.last() }
                println("Sorted by the last letter descending: $sortedByLast")
        }

        @Test
        fun test5(){
                val numbers = listOf("one", "two", "three", "four")
                println("Sorted by length ascending: ${numbers.sortedWith(compareBy { it.length })}")
        }

        @Test
        fun test6(){
                val numbers = listOf("one", "two", "three", "four")
                println(numbers.reversed())
        }

        @Test
        fun test7(){
                val numbers = listOf("one", "two", "three", "four")
                val reversedNumbers = numbers.asReversed()
                println(reversedNumbers)
        }

        @Test
        fun test8(){
                val numbers = mutableListOf("one", "two", "three", "four")
                val reversedNumbers = numbers.asReversed()
                println(reversedNumbers)
                numbers.add("five")
                println(reversedNumbers)
        }

        @Test
        fun test9(){
                val numbers = listOf("one", "two", "three", "four")
                println(numbers.shuffled())
        }


}

class Version(val major: Int, val minor: Int) : Comparable<Version> {
        override fun compareTo(other: Version): Int {
                if (this.major != other.major) {
                        return this.major - other.major
                } else if (this.minor != other.minor) {
                        return this.minor - other.minor
                } else return 0
        }
}

/*
        collection sorting
        1,sort()
        2,sortBy(),sortedBy()
        3,sortWith(),sortedWith()

 */