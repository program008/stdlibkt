package com.canbot.stdlibkt

import org.junit.Test

/**
 * Created by tao.liu on 2018/12/24.
 * description this is description
 */
class FunctionUnitTest {
        //let function
        @Test
        fun letTest01() {
                val str = "Hello World"
                str.let {
                        println("$it!!")
                }
                println(str)

                var strLength = str.let { "$it function".length }
                println("strLength is $strLength") //prints strLength is 25

        }

        @Test
        fun letTest02() {

                var a = 1
                var b = 2

                a = a.let { it + 2 }.let {
                        val i = it + b
                        i
                }
                println(a) //5
        }

        @Test
        fun letTest03() {
                var x = "Anupam"
                x.let { outer ->
                        outer.let { inner -> print("Inner is $inner and outer is $outer") }
                }
                //Prints
                //Inner is Anupam and outer is Anupam
        }

        @Test
        fun letTest04() {
                var x = "Anupam"
                x = x.let { outer ->
                        outer.let { inner ->
                                println("Inner is $inner and outer is $outer")
                                "Kotlin Tutorials Inner let"
                        }
                        "Kotlin Tutorials Outer let"
                }
                println(x) //prints Kotlin Tutorials Outer let

        }

        @Test
        fun letTest05() {
                var name: String? = "Kotlin let null check"
                name?.let { println(it) } //prints Kotlin let null check
                name = null
                name?.let { println(it) } //nothing happens

        }

        //run function

        @Test
        fun runTest01() {
                var tutorial = "This is Kotlin Tutorial"
                println(tutorial) //This is Kotlin Tutorial
                tutorial = run {
                        val tutorial = "This is run function"
                        tutorial
                }
                println(tutorial) //This is run function

        }

        /**
         *
         * Kotlin run expression can change the outer property. Hence in the above code, we’ve redefined it for the local scope.
         *      Similar to the let function, the run function also returns the last statement.
         *      Unlike let, the run function doesn’t support the it keyword.
         */

        //let and run

        @Test
        fun test02() {
                var p: String? = "ff"
                p?.let {
                        println("p is $p")
                } ?: run {
                        println("p was null. Setting default value to: ")
                        p = "Kotlin"
                }
                println(p)
                //Prints
                //p was null. Setting default value to:
                //Kotlin
        }

        //also function
        @Test
        fun alsoTest01() {
                var m = 1
                m = m.also {
                        it + 1
                }
                        .also {
                                it + 1
                        }
                println(m) //prints 1

        }

        // let vs also
        data class Person(var name: String, var tutorial: String)

        @Test
        fun test03() {
                var person = Person("Anupam", "Kotlin")
                var l = person.let {
                        it.tutorial = "Android"
                }
                var al = person.also {
                        it.tutorial = "Android"
                }

                println(l)
                println(al)
                println(person)

        }

        // apply function
        @Test
        fun applyTest01() {
                var person = Person("Anupam", "Kotlin")
                person.apply {
                        tutorial = "Swift"
                }
                println(person)
        }

        // apply vs also
        @Test
        fun test04() {
                data class Person(var n: String, var t: String)

                var person = Person("Anupam", "Kotlin")
                person.apply {
                        t = "Swift"
                }
                println(person)

                person.also {
                        it.t = "Android"
                }
                println(person)

        }

        // with function
        @Test
        fun withTest01() {
                var person = Person("Anupam", "Kotlin")
                with(person) {
                        name = "No Name"
                        tutorial = "Kotlin tutorials"
                }
                println(person)
        }

        /**
         * Kotlin apply vs with
         *      with runs without an object(receiver) whereas apply needs one.
         *      apply runs on the object reference, whereas with just passes it as an argument.
         *      The last expression of with function returns a result.
         */

        @Test
        fun Test05() {
                var person = Person("Anupam", "Kotlin")
                var xyz = with(person) {
                        name = "No Name"
                        tutorial = "Kotlin tutorials"
                        val xyz = "End of tutorial"
                        xyz
                }
                println(xyz) //End of tutorial

        }

        /*
                let vs also
                        1,都是支持it
                        2,let 返回最后一个语句作为返回值 also返回自身对象的引用

                also vs apply
                        1,都是返回自身对象的引用
                        2,前者支持it，后者不支持
                run vs with
                        1,都返回最后一个语句作为返回值
                        2,run 修改外部属性

         */

}