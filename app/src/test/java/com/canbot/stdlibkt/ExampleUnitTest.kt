package com.canbot.stdlibkt

import org.junit.Test

import org.junit.Assert.*
import java.util.regex.Pattern
import kotlin.properties.Delegates

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
        @Test
        fun addition_isCorrect() {
                assertEquals(4, 2 + 2)
        }

        var x = 0
        @Test
        fun main() {
                //函数引用
                val args = listOf(12, 23, 34, 25)
                args.filter(::isOld)
                        .map { "$it " }
                        .forEach(::print)
                println()
                //属性引用
                println(::x.get())
                ::x.set(2)
                println(x)
        }

        private fun isOld(age: Int): Boolean = age > 20

        @Test
        fun delegates(){
                val person = Person().apply { address = "南京" }
                person.address = "北京"
                person.address = "上海"
                person.address = "广州"
                println("address is ${person.address}")
        }

        @Test
        fun test(){
                val str:String = "let,also,run,with,apply"
                val result = str.also {
                        println(this)
                        println(it)
                        28
                }
                println(result)
        }

        @Test
        fun test2(){
               /* val text = "what's the weather like in beijing"
                val ins = text.split("in".toRegex(), 2).toTypedArray()
                for (i in ins.indices) {
                        println("ExampleUnitTest" + ins[i])
                }*/

                val text = "null"
                println(!"null".equals(text,true))
        }

        @Test
        fun test4(){
                val str = "what is the weather";
                val regular90 = "^.*(the weather|the temperature).*(in).*$"
                val pattern90:Pattern = Pattern.compile(regular90)
                var find = pattern90.matcher(str).find()
                println("是否匹配："+find)
        }
}

class Person {
        var address:String by Delegates.notNull()
        //属性代理Delegates.observable()的使用
        /*var address: String by Delegates.observable(initialValue = "南京", onChange = { property, oldValue, newValue ->
                println("property: ${property.name}  oldValue: $oldValue  newValue: $newValue")
        })*/

        /**
         * If the callback returns `true` the value of the property is being set to the new value,
         * and if the callback returns `false` the new value is discarded and the property remains its old value.
         */
        /*var address: String by Delegates.vetoable(initialValue = "南京", onChange = { property, oldValue, newValue ->
                println("property: ${property.name}  oldValue: $oldValue  newValue: $newValue")
                return@vetoable newValue == "北京"
        })*/
}
