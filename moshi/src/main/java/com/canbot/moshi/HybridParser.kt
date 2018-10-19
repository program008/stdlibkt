package com.canbot.moshi

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi

class HybridParser(moshi: Moshi) {
    private val personAdapter: JsonAdapter<Person> = moshi.adapter(Person::class.java)

    fun parse(reader: JsonReader): List<Person> {
        return reader.readArrayToList {
            personAdapter.fromJson(reader)?.takeIf { it.age >= 18 }
        }
    }
}