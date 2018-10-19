package com.canbot.moshi

import com.squareup.moshi.JsonClass

/**
 * Created by tao.liu on 2018/10/12.
 * description this is description
 */
@JsonClass(generateAdapter = true)
class Person(val id:Long,val name:String,val age:Int = -1)