package com.uurobot.kt.collection

/**
 *
 * Created by tao.liu on 2019/6/2.
 */

//嵌套类
//嵌套类不能访问外部类的属性
class Outer {
        private val bar: Int = 1

        class Nested {
                fun foo() = 1
        }
}

private fun test() {
        val demo = Outer.Nested().foo() // == 2
}

// 内部类
// 类可以标记为 inner 以便能够访问外部类的成员。内部类会带有一个对外部类的对象的引用：
class Outer2 {
        private val bar: Int = 1

        inner class Inner {
                fun foo() = bar
        }
}

private fun test2() {
        val demo = Outer2().Inner().foo() // == 1
}

interface ActionListener {
        fun click(index: Int)
}

fun registerActionListener(actionListener: ActionListener) {}
private fun test3() {
        registerActionListener(actionListener = object : ActionListener {
                override fun click(index: Int) {

                }
        })
        registerActionListener(object : ActionListener {
                override fun click(index: Int) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
        })

        //registerActionListener()
}