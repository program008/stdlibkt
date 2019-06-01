package com.uurobot.kt.extensions

import com.uurobot.kt.MyClass

/**
 *
 * Created by tao.liu on 2019/6/1.
 */
/**
 * 扩展函数也可以作用在泛型
 */
fun <T>MutableList<T>.swap(index1:Int,index2:Int){
        val temp = this[index1]
        this[index1] = this[index2]
        this[index2] = temp
}

fun Any?.toString(): String {
        if (this == null) return "null"
        // 空检测之后，“this”会自动转换为非空类型，所以下面的 toString()
        // 解析为 Any 类的成员函数
        return toString()
}

/**
 * 扩展属性
 *
 * 注意：由于扩展没有实际的将成员插入类中，因此对扩展属性来说幕后字段是无效的。
 * 这就是为什么扩展属性不能有初始化器。他们的行为只能由显式提供的 getters/setters 定义
 */
val <T>List<T>.lastIndex:Int
        get() = size - 1

/**
 * 伴生对象扩展函数
 */
fun MyClass.Companion.foo():String = "这是一个伴生对象的扩展函数"


/**
 * 扩展声明为成员
 * 在一个类内部你可以为另一个类声明扩展。在这样的扩展内部，有多个 隐式接收者 ——
 * 其中的对象成员可以无需通过限定符访问。
 * 扩展声明所在的类的实例称为 分发接收者，扩展方法调用所在的接收者类型的实例称为 扩展接收者 。
 */
class D {
        fun bar() { println("bar()---D")}
        fun baz() { println("baz()---D")}
}

class C {
        fun baz() {
                println("baz()")}

        fun D.foo() {
                bar()   // 调用 D.bar
                //对于分发接收者与扩展接收者的成员名字冲突的情况，扩展接收者优先。
                // 要引用分发接收者的成员你可以使用 限定的 this 语法。
                baz()   // 调用 D.baz
                this@C.baz()//调用C.baz
        }

        fun caller(d: D) {
                d.foo()   // 调用扩展函数
        }
}

fun main(){
        val c = C()
        c.caller(D())
}