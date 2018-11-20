package com.canbot.moshi

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
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
                val reader = "{\n" +
                        "    \"id\": 1,\n" +
                        "    \"name\": \"John\",\n" +
                        "    \"age\": 38\n" +
                        "  }"

                val moshi = Moshi.Builder().build()
                val jsonAdapter:JsonAdapter<Person> = moshi.adapter<Person>(Person::class.java)
                val person:Person = jsonAdapter.fromJson(reader)!!
                val result = person.let {
                        println("id = ${it.id} name = ${it.name} age = ${it.age}")
                        it.name
                }
                println(result)

                val person2:Person = PersonJsonAdapter(moshi).fromJson(reader)!!
                println(person2.name)

        }
}
