package com.uurobot.kt.collection

/**
 *
 * Created by tao.liu on 2019/6/2.
 * 密封类用来表示受限的类继承结构：当一个值为有限集中的类型、而不能有任何其他类型时。
 */

sealed class Expr

data class Const(val number: Double) : Expr()
data class Sum(val e1: Expr, val e2: Expr) : Expr()
object NotANumber : Expr()

fun eval(expr: Expr): Double = when (expr) {
        is Const -> expr.number
        is Sum -> eval(expr.e1) + eval(expr.e2)
        is NotANumber -> Double.NaN
}

fun main() {
        eval(Sum(Const(1.0), Sum(Const(2.0), Const(3.0)))).also(::println)
}