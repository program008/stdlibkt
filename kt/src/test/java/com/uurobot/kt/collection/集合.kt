package com.uurobot.kt.collection

/**
 *
 * Created by tao.liu on 2019/6/2.
 */
fun main() {
        /*val numbers = mutableListOf(1, 2, 3, 4)
        numbers.add(5)
        numbers.removeAt(1)
        numbers[0] = 0
        //numbers.shuffle()
        numbers.reverse()
        println(numbers)*/

        val numbers = setOf(1, 2, 3, 4)  // LinkedHashSet is the default implementation
        val numbersBackwards = setOf(4, 3, 2, 1)

        println(numbers.first() == numbersBackwards.first())
        println(numbers.first() == numbersBackwards.last())
}

