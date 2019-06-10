package com.uurobot.kt.collection

/**
 *
 * Created by tao.liu on 2019/6/1.
 */

/*
为了确保生成的代码的一致性以及有意义的行为，数据类必须满足以下要求：

主构造函数需要至少有一个参数；
主构造函数的所有参数需要标记为 val 或 var；
数据类不能是抽象、开放、密封或者内部的；
（在1.1之前）数据类只能实现接口。
此外，成员生成遵循关于成员继承的这些规则：

如果在数据类体中有显式实现 equals()、 hashCode() 或者 toString()，

或者这些函数在父类中有 final 实现，那么不会生成这些函数，而会使用现有函数；
如果超类型具有 open 的 componentN() 函数并且返回兼容的类型， 那么会为数据类生成相应的函数，
并覆盖超类的实现。如果超类型的这些函数由于签名不兼容或者是 final 而导致无法覆盖，那么会报错；
从一个已具 copy(……) 函数且签名匹配的类型派生一个数据类在 Kotlin 1.2 中已弃用，并且在 Kotlin 1.3 中已禁用。
不允许为 componentN() 以及 copy() 函数提供显式实现。
 */

//在 JVM 中，如果生成的类需要含有一个无参的构造函数，则所有的属性必须指定默认值。
data class User(val name: String = "", val age: Int = 0)

//对于那些自动生成的函数，编译器只使用在主构造函数内部定义的属性。
// 如需在生成的实现中排出一个属性，请将其声明在类体中：
data class Person(val name: String) {
        var age: Int = 0
}


//复制
//fun copy(name: String = this.name, age: Int = this.age) = User(name, age)

val jack = User(name = "Jack", age = 1)
val olderJack = jack.copy(age = 2)

data class Student(val name:String,val age:Int)

fun main() {
        val jane = Student("Jane", 35)
        //数据类与解构声明
        val (name, age) = jane
        println("$name, $age years of age") // 输出 "Jane, 35 years of age"

}

