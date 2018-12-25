package com.canbot.stdlibkt

import org.junit.Test
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Created by tao.liu on 2018/11/23.
 * description this is description
 */
class UnitTest01 {
        @Test
        fun fun1() {
                val numbers = listOf(1, -2, 3, -4, 5, -6)            // 1

                val totalCount = numbers.count()                     // 2
                val evenCount = numbers.count { it % 2 == 0 }        // 3

                println("Total number of elements: $totalCount")
                println("Number of even elements: $evenCount")
        }

        fun foo(string: String?) {
               /* if (string.isNotNull()) {
                        string.length
                }*/
        }


        //fun String?.isNotNull() = this != null

       /* @ExperimentalContracts
        fun String?.isNotNull(): Boolean {
                contract {
                        returns(true) implies(this@isNotNull != null)
                }
                return this != null
        }

        @ExperimentalContracts
        fun String?.isNullOrEmpty(): Boolean {
                contract { returns(false) implies (this@isNullOrEmpty != null) }
                return this == null || isEmpty()
        }*/

        @Test
        fun test02(){
                data class Person(val name: String, val city: String, val phone: String) // 1

                val people = listOf(                                                     // 2
                        Person("John", "Boston", "+1-888-123456"),
                        Person("Sarah", "Munich", "+49-777-789123"),
                        Person("Svyatoslav", "Saint-Petersburg", "+7-999-456789"),
                        Person("Vasilisa", "Saint-Petersburg", "+7-999-123456"))

                val phoneBook = people.associateBy { it.phone }                          // 3
                val cityBook = people.associateBy(Person::phone, Person::city)           // 4
                val peopleCities = people.groupBy(Person::city, Person::name)            // 5


                println("People: $people")
                println("Phone book: $phoneBook")
                println("City book: $cityBook")
                println("People living in each city: $peopleCities")
        }
}