package com.uurobot.kt

import java.util.function.BinaryOperator
import java.util.function.IntBinaryOperator

/**
 *
 * Created by tao.liu on 2019/6/2.
 */
enum class Direction {
        NORTH, SOUTH, WEST, EAST
}

fun main() {
        var values = Direction.values()
        var valueOf = Direction.valueOf("NORTH")
        println(valueOf.name+"," +valueOf.ordinal)
}
enum class Color(val rgb: Int) {
        RED(0xFF0000),
        GREEN(0x00FF00),
        BLUE(0x0000FF)
}
//在枚举类中实现接口
enum class IntArithmetics : BinaryOperator<Int>, IntBinaryOperator {
        PLUS {
                override fun apply(t: Int, u: Int): Int = t + u
        },
        TIMES {
                override fun apply(t: Int, u: Int): Int = t * u
        };

        override fun applyAsInt(t: Int, u: Int) = apply(t, u)
}

private fun test1(){
        val a = 13
        val b = 31
        for (f in IntArithmetics.values()) {
                println("$f($a, $b) = ${f.apply(a, b)}")
        }
}


