package com.canbot.moshi

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader

/**
 * Created by tao.liu on 2018/10/12.
 * description this is description
 */
class ManualParser {
        companion object {
                val NAMES = JsonReader.Options.of("id", "name", "age")
        }

        fun parser(reader: JsonReader): List<Person> {
                /* val result = mutableListOf<Person>()
                 reader.beginArray()
                 while (reader.hasNext()) {
                         var id: Long = -1L
                         var name: String = ""
                         var age: Int = -1
                         reader.beginObject()
                         while (reader.hasNext()) {
                                 when (reader.selectName(NAMES)) {
                                         0 -> id = reader.nextLong()
                                         1 -> name = reader.nextString()
                                         2 -> age = reader.nextInt()
                                         else -> {
                                                 reader.skipName()
                                                 reader.skipValue()
                                         }
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
                 return result*/

                return reader.readArrayToList {
                        var id: Long = -1L
                        var name: String = ""
                        var age: Int = -1

                        reader.readObject {
                                when (reader.selectName(NAMES)) {
                                        0 -> id = reader.nextLong()
                                        1 -> name = reader.nextString()
                                        2 -> age = reader.nextInt()
                                        else -> {
                                                reader.skipNameAndValue()
                                        }
                                }
                        }

                        if (id == -1L || name == "") {
                                throw JsonDataException("Missing required field")
                        }

                        Person(id, name, age)
                }
        }
}