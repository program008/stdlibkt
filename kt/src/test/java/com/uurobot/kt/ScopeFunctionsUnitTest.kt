package com.uurobot.kt

import org.junit.Test
import kotlin.random.Random

/**
 *
 * Created by tao.liu on 2019/5/26.
 * scope functions usage
 *
 * let
 * The context object is available as an argument (it). The return value is the lambda result.
 *
 * with=>> “with this object, do the following.”
 * A non-extension function: the context object is passed as an argument, but inside the lambda,
 * it's available as a receiver (this). The return value is the lambda result.
 *
 * run
 * The context object is available as a receiver (this). The return value is the lambda result.
 *
 * apply=>> “apply the following assignments to the object.”
 * The context object is available as a receiver (this). The return value is the object itself.
 *
 * also=>> “and also do the following”.
 * The context object is available as an argument (it). The return value is the object itself.
 */
class ScopeFunctionsUnitTest {
        @Test
        fun test1(){
                val str = "Hello"
                // this
                str.run {
                        println("The receiver string length: $length")
                        //println("The receiver string length: ${this.length}") // does the same

                }

                // it
                str.let {
                        println("The receiver string's length is ${it.length}")
                }
        }

        data class Person(var name: String, var age: Int = 0, var city: String = "")
        @Test
        fun test2() {
                val adam = Person("Adam").apply {
                        age = 20                       // same as this.age = 20 or adam.age = 20
                        city = "London"
                }
        }

        fun writeToLog(message: String) {
                println("INFO: $message")
        }

        @Test
        fun test3() {
                fun getRandomInt(): Int {
                        return Random.nextInt(100).also {
                                writeToLog("getRandomInt() generated value $it")
                        }
                }

                val i = getRandomInt()
        }

        @Test
        fun test4(){
                fun getRandomInt(): Int {
                        return Random.nextInt(100).also { value ->
                                writeToLog("getRandomInt() generated value $value")
                        }
                }

                val i = getRandomInt()
        }

        @Test
        fun test5(){
                val numberList = mutableListOf<Double>()
                numberList.also { println("Populating the list") }
                        .apply {
                                add(2.71)
                                add(3.14)
                                add(1.0)
                        }
                        .also { println("Sorting the list") }
                        .sort()
                println(numberList)
        }

        @Test
        fun test6(){
                fun getRandomInt(): Int {
                        return Random.nextInt(100).also {
                                writeToLog("getRandomInt() generated value $it")
                        }
                }

                val i = getRandomInt()
        }

        @Test
        fun test7(){
                val numbers = mutableListOf("one", "two", "three")
                val countEndsWithE = numbers.run {
                        add("four")
                        add("five")
                        count { it.endsWith("e") }
                }
                println("There are $countEndsWithE elements that end with e.")
        }

        @Test
        fun test8(){
                val numbers = mutableListOf("one", "two", "three")
                with(numbers) {
                        val firstItem = first()
                        val lastItem = last()
                        println("First item: $firstItem, last item: $lastItem")
                }
        }

        /**
         * If the code block contains a single function with it as an argument,
         * you can use the method reference (::) instead of the lambda:
         */
        @Test
        fun test9(){
                val numbers = mutableListOf("one", "two", "three", "four", "five")
                numbers.map { it.length }.filter { it > 3 }.let(::println)
        }
        fun processNonNullString(str: String) {}
        @Test
        fun test10(){
                val str: String? = "Hello"
                //processNonNullString(str)       // compilation error: str can be null
                val length = str?.let {
                        println("let() called on $it")
                        processNonNullString(it)      // OK: 'it' is not null inside '?.let { }'
                        it.length
                }
        }

        @Test
        fun test11(){
                val numbers = listOf("one", "two", "three", "four")
                val modifiedFirstItem = numbers.first().let { firstItem ->
                        println("The first item of the list is '$firstItem'")
                        if (firstItem.length >= 5) firstItem else "!" + firstItem + "!"
                }.toUpperCase()
                println("First item after modifications: '$modifiedFirstItem'")
        }

        @Test
        fun test12(){
                val numbers = mutableListOf("one", "two", "three")
                with(numbers) {
                        println("'with' is called with argument $this")
                        println("It contains $size elements")
                }
        }

        @Test
        fun test13(){
                val numbers = mutableListOf("one", "two", "three")
                val firstAndLast = with(numbers) {
                        "The first element is ${first()}," +
                                " the last element is ${last()}"
                }
                println(firstAndLast)
        }

        class MultiportService(var url: String, var port: Int) {
                fun prepareRequest(): String = "Default request"
                fun query(request: String): String = "Result for query '$request'"
        }
        @Test
        fun test14() {
                val service = MultiportService("https://example.kotlinlang.org", 80)

                val result = service.run {
                        port = 8080
                        query(prepareRequest() + " to port $port")
                }

                // the same code written with let() function:
                val letResult = service.let {
                        it.port = 8080
                        it.query(it.prepareRequest() + " to port ${it.port}")
                }
                println(result)
                println(letResult)
        }

        @Test
        fun test15(){
                val hexNumberRegex = run {
                        val digits = "0-9"
                        val hexDigits = "A-Fa-f"
                        val sign = "+-"

                        Regex("[$sign]?[$digits$hexDigits]+")
                }

                for (match in hexNumberRegex.findAll("+1234 -FFFF not-a-number")) {
                        println(match.value)
                }
        }


        @Test
        fun test16() {
                val adam = Person("Adam").apply {
                        age = 32
                        city = "London"
                }

                println(adam)
        }

        @Test
        fun test17(){
                val numbers = mutableListOf("one", "two", "three")
                numbers
                        .also { println("The list elements before adding new one: $it") }
                        .add("four")
        }

        @Test
        fun test18(){
                val number = Random.nextInt(100)

                val evenOrNull = number.takeIf { it % 2 == 0 }
                val oddOrNull = number.takeUnless { it % 2 == 0 }
                println("even: $evenOrNull, odd: $oddOrNull")
        }

        @Test
        fun test19(){
                fun displaySubstringPosition(input: String, sub: String) {
                        input.indexOf(sub).takeIf { it >= 0 }?.let {
                                println("The substring $sub is found in $input.")
                                println("Its start position is $it.")
                        }
                }

                displaySubstringPosition("010000011", "11")
                displaySubstringPosition("010000011", "12")
        }

}