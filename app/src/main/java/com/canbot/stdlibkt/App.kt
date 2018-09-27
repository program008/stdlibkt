package com.canbot.stdlibkt

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

/**
 * Created by tao.liu on 2018/9/27.
 * description this is description
 */
class App: Application() {
        override fun onCreate() {
                super.onCreate()
                context = applicationContext
        }

        companion object {
                var context: Context by Delegates.notNull()
                        private set
        }
}