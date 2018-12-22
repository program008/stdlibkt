package com.canbot.coroutinekt

import kotlinx.coroutines.CompletableDeferred

/**
 * Created by tao.liu on 2018/12/22.
 * description this is description
 */
// 计数器 Actor 的各种类型
sealed class CounterMsg

object IncCounter : CounterMsg() // 递增计数器的单向消息
class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg() // 携带回复的请求
