package com.example.anko

import org.junit.Test

import org.junit.Assert.*

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

        @Test
        fun test(){
                var  s = "10236|1006|"
                s = s.substring(0,s.lastIndexOf("|"))
                var split = s.split("|")
                split.forEach {
                        println("舞蹈："+it)
                }
        }
}
