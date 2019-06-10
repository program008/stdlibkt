package com.uurobot.kt.collection

import org.junit.Test

/**
 * Created by tao.liu on 2019/6/10.
 * description this is description
 */
class CollectionTranslate {
        @Test
        fun test1() {
                val numbers = setOf(1, 2, 3)
                println(numbers.map { it * 3 })
                println(numbers.mapIndexed { idx, value -> value * idx })
        }

        @Test
        fun test2() {
                val numbers = setOf(1, 2, 3)
                println(numbers.mapNotNull { if (it == 2) null else it * 3 })
                println(numbers.mapIndexedNotNull { idx, value -> if (idx == 0) null else value * idx })
        }

        @Test
        fun test3() {
                val numbersMap = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key11" to 11)
                println(numbersMap.mapKeys { it.key.toUpperCase() })
                println(numbersMap.mapValues { it.value + it.key.length })
        }

        @Test
        fun test4() {
                val colors = listOf("red", "brown", "grey")
                val animals = listOf("fox", "bear", "wolf")
                println(colors zip animals)

                val twoAnimals = listOf("fox", "bear")
                println(colors.zip(twoAnimals))
        }

        @Test
        fun test5() {
                val colors = listOf("red", "brown", "grey")
                val animals = listOf("fox", "bear", "wolf")

                println(colors.zip(animals) { color, animal -> "The ${animal.capitalize()} is $color" })
        }

        @Test
        fun test6() {
                val numberPairs = listOf("one" to 1, "two" to 2, "three" to 3, "four" to 4)
                println(numberPairs.unzip())
        }

        @Test
        fun test7() {
                val numbers = listOf("one", "two", "three", "four")
                println(numbers.associateWith { it.length })
        }

        @Test
        fun test8() {
                val numbers = listOf("one", "two", "three", "four")

                println(numbers.associateBy { it.first().toUpperCase() })
                println(numbers.associateBy(keySelector = { it.first().toUpperCase() }, valueTransform = { it.length }))
        }

        @Test
        fun test9() {
                data class FullName(val firstName: String, val lastName: String)

                fun parseFullName(fullName: String): FullName {
                        val nameParts = fullName.split(" ")
                        if (nameParts.size == 2) {
                                return FullName(nameParts[0], nameParts[1])
                        } else throw Exception("Wrong name format")
                }

                val names = listOf("Alice Adams", "Brian Brown", "Clara Campbell")
                println(names.associate { name -> parseFullName(name).let { it.lastName to it.firstName } })
        }

        @Test
        fun test10() {
                val numberSets = listOf(setOf(1, 2, 3), setOf(4, 5, 6), setOf(1, 2))
                println(numberSets.flatten())
        }

        data class StringContainer(val values: List<String>)

        @Test
        fun test11() {
                val containers = listOf(
                        StringContainer(listOf("one", "two", "three")),
                        StringContainer(listOf("four", "five", "six")),
                        StringContainer(listOf("seven", "eight"))
                )
                println(containers.flatMap { it.values })
        }

        @Test
        fun test12() {
                val numbers = listOf("one", "two", "three", "four")

                println(numbers)
                println(numbers.joinToString())

                val listString = StringBuffer("The list of numbers: ")
                numbers.joinTo(listString)
                println(listString)
        }

        @Test
        fun test13() {
                val numbers = listOf("one", "two", "three", "four")
                println(numbers.joinToString(separator = " | ", prefix = "start: ", postfix = ": end"))
        }

        @Test
        fun test14() {
                val numbers = (1..100).toList()
                println(numbers.joinToString(limit = 10, truncated = "<...>"))
        }

        @Test
        fun test15() {
                val numbers = listOf("one", "two", "three", "four")
                println(numbers.joinToString{ "Element: ${it.toUpperCase()}" })
        }


}

