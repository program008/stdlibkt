package com.canbot.coroutinekt

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Handler.Callback
import android.os.Message
import android.util.Log

class MainActivity : AppCompatActivity() {

        private var handler:Handler = Handler(Callback { p0 ->
                handleMessage(p0)
                false
        })

        private fun handleMessage(p0: Message?) {
                when (p0?.what) {
                        22 -> {
                                this@MainActivity.handler.sendEmptyMessage(33)
                        }

                        233 -> this@MainActivity.handler.sendEmptyMessage(33)
                }
        }
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)
                handler.sendEmptyMessage(333)
        }
}
