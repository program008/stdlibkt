package com.uurobot.kt

/**
 *
 * Created by tao.liu on 2019/6/1.
 */
class ClassesUnitTest {

}

open class Base {
        open val x: Int
                get() {
                        return 0
                }

        open fun f() {}
        fun fn() {}
}

/**
 * 属性初始化中，可以用一个var 覆盖一个val 反正不行，
 */
open class Derived() : Base() {
        override val x: Int
                get() = 2

        override fun f() {
                TODO("do some thing")
        }
}

//override 修饰的方法本身是open的，如果不想被子类覆盖，那么
//需要将父类这个override修饰的方法用final修饰，这样子类就不能再覆盖父类的该方法了。
class AnotherDerived() : Derived() {
        override fun f() {
        }
}

/**
 * 派生类初始化顺序
 *
 * 初始化结果：
 * Argument for Base: Hello
 * Initializing Base
 * Initializing size in Base: 5
 * Initializing Derived
 * Initializing size in Derived: 10
 *
 * 设计一个基类时，应该避免在构造函数、属性初始化器以及 init 块中使用 open 成员。
 */
open class Base_(val name: String) {

        init { println("Initializing Base") }

        open val size: Int =
                name.length.also { println("Initializing size in Base: $it") }
}

class Derived_(
        name: String,
        val lastName: String
) : Base_(name.capitalize().also { println("Argument for Base: $it") }) {

        init { println("Initializing Derived") }

        override val size: Int =
                (super.size + lastName.length).also { println("Initializing size in Derived: $it") }
}

/**
 * 调用超类实现
 * 派生类中的代码可以使用 super 关键字调用其超类的函数与属性访问器的实现：
 */
open class Foo {
        open fun f() { println("Foo.f()") }
        open val x: Int get() = 1
}

class Bar : Foo() {
        override fun f() {
                super.f()
                println("Bar.f()")
        }

        override val x: Int get() = super.x + 1
}

// 在一个内部类中访问外部类的超类，可以通过由外部类名限定的 super 关键字来实现：super@Outer：
class Bar_ : Foo() {
        override fun f() { /* …… */ }
        override val x: Int get() = 0

        inner class Baz {
                fun g() {
                        super@Bar_.f() // 调用 Foo 实现的 f()
                        println(super@Bar_.x) // 使用 Foo 实现的 x 的 getter
                }
        }
}
//覆盖规则
open class A {
        open fun f() { print("A") }
        fun a() { print("a") }
}

interface B {
        fun f() { print("B") } // 接口成员默认就是“open”的
        fun b() { print("b") }
}

class C() : A(), B {
        // 编译器要求覆盖 f()：
        override fun f() {
                super<A>.f() // 调用 A.f()
                super<B>.f() // 调用 B.f() 消除歧义
        }
}

class MyClass {
        companion object { }  // 将被称为 "Companion"
}








