package com.uurobot.kt

/**
 *
 * Created by tao.liu on 2019/6/2.
 */
private fun test() {
        val box = Box(1)
        var value = box.value
}

class Box<T>(t: T) {
        val value = t
}

//声明处型变（declaration-site variance）与类型投影（type projections）
//ava 中的泛型是不型变的，这意味着 List<String> 并不是 List<Object> 的子类型。

//声明处型变

/*
一般原则是：当一个类 C 的类型参数 T 被声明为 out 时，它就只能出现在 C 的成员的输出-位置，
但回报是 C<Base> 可以安全地作为 C<Derived>的超类。

简而言之，他们说类 C 是在参数 T 上是协变的，或者说 T 是一个协变的类型参数。
你可以认为 C 是 T 的生产者，而不是 T 的消费者。

out修饰符称为型变注解，并且由于它在类型参数声明处提供，所以我们称之为声明处型变。
这与 Java 的使用处型变相反，其类型用途通配符使得类型协变。
 */

interface Source<out T> {
        fun next(): T
}


fun demo(str: Source<String>) {
        val objects: Source<Any> = str
}

//逆变：只可以被消费而不可以被生产。
interface Comparable<in T> {
        operator fun compareTo(other: T): Int
}

fun demo2(x: Comparable<Number>) {
        x.compareTo(1.0) // 1.0 拥有类型 Double，它是 Number 的子类型
        // 因此，我们可以将 x 赋给类型为 Comparable <Double> 的变量
        val y: Comparable<Double> = x // OK！
}

//存在性（The Existential） 转换：消费者 in, 生产者 out! :-)


//使用处型变：类型投影
//class Array<T>(val size: Int) {
//        fun get(index: Int): T  = get(index)
//        fun set(index: Int, value: T) {  }
//}

fun copy(from: Array<out Any>, to: Array<Any>) {
        assert(from.size == to.size)
        for (i in from.indices)
                to[i] = from[i]
}

fun fill(dest: Array<in String>, value: String) {}
fun main() {
        val ints: Array<Int> = arrayOf(1, 2, 3)
        val any = Array<Any>(3) { "" }
        copy(ints, any)
        any.also { println(any[2]) }
//   ^ 其类型为 Array<Int> 但此处期望 Array<Any>
}

//泛型函数
//不仅类可以有类型参数。函数也可以有。类型参数要放在函数名称之前：
fun <T> singletonList(item: T): List<T> {
        // ……
        return listOf()
}

fun <T> T.basicToString(): String {  // 扩展函数
        // ……
        return ""
}

private fun test2() {
        var singletonList = singletonList(1)

        var basicToString = String.basicToString()
}


//星投影

// 1,星号投影不能写入，只能读取。因为星号投影的类型不确定，所以写入的任何值都有可能跟原有的类型冲空；
// 但星号投影可以读取，因为任何类型都有一个父类 Any?
// 2,kt 中 <*> 等价于 java 中的 <?>
// 3,星号投影不需要使用任何在签名中引用类型参数的方法。
// 4,星号投影适用于只是读取数据，而不关心其具体类型。

private fun test3() {
        val l: ArrayList<*> = arrayListOf("", 1, 2, 3)
//    Error: Out-projected type 'MutableList<*>' prohibits the use of
//    'fun add(element: E): Boolean'
//     l.add(1)
        val get = l[0]
}
//类型擦除

