package com.uurobot.kt

import org.junit.Test

import org.junit.Assert.*
import kotlin.experimental.and

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
                //0x20, 0xeb /0x00, 0x27
                var dataLen = getDataLen(0x00, 0x27)
                var dataLen2 = getDataLen2(0x00, 0x27)
                println("$dataLen, $dataLen2")

        }

        fun getDataLen(hight: Byte, low: Byte): Int {
                val iHight = hight and 0xff.toByte()
                val iLow = low and 0xff.toByte()
                return iHight * 256 + iLow
        }

        fun getDataLen2(hight: Byte, low: Byte): Int {
                val iHight = hight.toInt() and 0xff
                val iLow = low.toInt() and 0xff
                return iHight * 256 + iLow
        }
}
