package com.canbot.moshi

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import kotlin.coroutines.experimental.buildSequence

class LazyParser(moshi: Moshi) {
    private val personAdapter: JsonAdapter<Person> = moshi.adapter(Person::class.java)

    fun parse(reader: JsonReader): Sequence<Person> {
        return buildSequence {
            reader.readArray {
                yield(personAdapter.fromJson(reader)!!)
            }
        }
    }
}