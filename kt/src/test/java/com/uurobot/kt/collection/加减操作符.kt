package com.uurobot.kt.collection

/**
 * Created by tao.liu on 2019/6/10.
 * description this is description
 */
fun main() {
        val numbers = listOf("one", "two", "three", "four")

        val plusList = numbers + "five"
        val minusList = numbers - listOf("three", "four")
        println(plusList)
        println(minusList)
        println(numbers)
}