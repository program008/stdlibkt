package com.canbot.stdlibkt.jsonparser

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader

class ManualParser {
        fun parse(reader: JsonReader): List<Person> {
                val result = mutableListOf<Person>()

                reader.beginArray()
                while (reader.hasNext()) {
                        var id: Long = -1L
                        var name: String = ""
                        var age: Int = -1

                        reader.beginObject()
                        while (reader.hasNext()) {
                                when (reader.nextName()) {
                                        "id" -> id = reader.nextLong()
                                        "name" -> name = reader.nextString()
                                        "age" -> age = reader.nextInt()
                                        else -> reader.skipValue()
                                }
                        }
                        reader.endObject()

                        if (id == -1L || name == "") {
                                throw JsonDataException("Missing required field")
                        }
                        val person = Person(id, name, age)
                        result.add(person)
                }
                reader.endArray()

                return result
        }
}