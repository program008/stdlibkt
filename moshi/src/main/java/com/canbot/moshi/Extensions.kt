package com.canbot.moshi

import com.squareup.moshi.JsonReader

/**
 * Created by tao.liu on 2018/10/12.
 * description this is description
 */
fun JsonReader.skipNameAndValue() {
        skipName()
        skipValue()
}
inline fun JsonReader.readObject(body: () -> Unit) {
        beginObject()
        while (hasNext()) {
                body()
        }
        endObject()
}

inline fun JsonReader.readArray(body: () -> Unit) {
        beginArray()
        while (hasNext()) {
                body()
        }
        endArray()
}

inline fun <T : Any> JsonReader.readArrayToList(body: () -> T?): List<T> {
        val result = mutableListOf<T>()
        beginArray()
        while (hasNext()) {
                body()?.let { result.add(it) }
        }
        endArray()
        return result
}