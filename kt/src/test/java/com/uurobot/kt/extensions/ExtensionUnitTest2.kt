package com.uurobot.kt.extensions

/**
 *
 * Created by tao.liu on 2019/6/1.
 * 声明为成员的扩展可以声明为 open 并在子类中覆盖。
 * 这意味着这些函数的分发对于分发接收者类型是虚拟的，但对于扩展接收者类型是静态的。
 */
open class D0 { }

class D1 : D0() { }

open class C0 {
        open fun D0.foo() {
                println("D.foo in C")
        }

        open fun D1.foo() {
                println("D1.foo in C")
        }

        fun caller(d: D0) {
                d.foo()   // 调用扩展函数
        }
}

class C1 : C0() {
        override fun D0.foo() {
                println("D.foo in C1")
        }

        override fun D1.foo() {
                println("D1.foo in C1")
        }
}

fun main() {
        C0().caller(D0())   // 输出 "D.foo in C"
        C1().caller(D0())  // 输出 "D.foo in C1" —— 分发接收者虚拟解析
        C0().caller(D1())  // 输出 "D.foo in C" —— 扩展接收者静态解析
}