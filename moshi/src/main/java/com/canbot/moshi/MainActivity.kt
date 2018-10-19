package com.canbot.moshi

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.canbot.moshi.databinding.ActivityMainBinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class MainActivity : AppCompatActivity() {
        lateinit var binding: ActivityMainBinding
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                //setContentView(R.layout.activity_main)
                binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
                //Advanced JSON parsing techniques using Moshi and Kotlin


                //val adapter: JsonAdapter<Person> = moshi.adapter(Person::class.java)

                //val listType = Types.newParameterizedType(List::class.java, Person::class.java)
                //val adapter: JsonAdapter<List<Person>> = moshi.adapter(listType)
                //val result = adapter.fromJson(reader)

                //val sequence = LazyParser(moshi).parse(reader)
                //val filteredList = sequence.filter { it.age >= 18 }.toList()

                val reader = "{\n" +
                        "    \"id\": 1,\n" +
                        "    \"name\": \"John\",\n" +
                        "    \"age\": 38\n" +
                        "  }"
                var obj = PersonJsonAdapter(moshi = Moshi.Builder().build()).fromJson(reader)
        }
}
