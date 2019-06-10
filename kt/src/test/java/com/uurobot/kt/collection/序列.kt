package com.uurobot.kt.collection

/**
 * Created by tao.liu on 2019/6/5.
 * description this is description
 */

fun main() {
        //构造

        val numbersSequence = sequenceOf("four", "three", "two", "one")

        val numbers = listOf("one", "two", "three", "four")
        val numbersSequence2 = numbers.asSequence()

        val oddNumbers = generateSequence(1) { it + 2 } // `it` is the previous element
        println(oddNumbers.take(5).toList())
        //println(oddNumbers.count())     // error: the sequence is infinite


        val oddNumbersLessThan10 = generateSequence(1) { if (it < 10) it + 2 else null }
        println(oddNumbersLessThan10.count())

        val oddNumbers2 = sequence {
                yield(1)
                yieldAll(listOf(3, 5))
                yieldAll(generateSequence(7) { it + 2 })
        }
        println(oddNumbers2.take(5).toList())

        test4()
}

private fun test1(){
        val numbers = listOf("one", "two", "three", "four")
        val filterResults = mutableListOf<String>()  //destination object
        numbers.filterTo(filterResults) { it.length > 3 }
        numbers.filterIndexedTo(filterResults) { index, _ -> index == 1 }
        println(filterResults) // contains results of both operations
}

private fun test2(){
        val numbers = listOf("one", "two", "three", "four")
        numbers.filter { it.length > 3 }  // nothing happens with `numbers`, result is lost
        println("numbers are still $numbers")
        val longerThan3 = numbers.filter { it.length > 3 } // result is stored in `longerThan3`
        println("numbers longer than 3 chars are $longerThan3")
}

private fun test3(){
        val numbers = listOf("one", "two", "three", "four")
        // filter numbers right into a new hash set,
        // thus eliminating duplicates in the result
        val result = numbers.mapTo(HashSet()) { it.length }
        println("distinct item lengths are $result")
}

private fun test4(){
        val numbers = mutableListOf("one", "two", "three", "four")
        val sortedNumbers = numbers.sorted()//不改变原数组
        println(numbers == sortedNumbers)  // false
        numbers.sort() //改变原数组
        println(numbers == sortedNumbers)  // true
}