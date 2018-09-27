package com.canbot.stdlibkt.mvvm

import android.content.Context
import android.net.ConnectivityManager
import com.canbot.stdlibkt.App

/**
 * Created by tao.liu on 2018/9/27.
 * description this is description
 */
class NetManager(application: Context) {
        private var status: Boolean? = false
        val context = application
        val isConnectedToInternet: Boolean?
                get() {
                        val conManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                        val ni = conManager.activeNetworkInfo
                        return ni != null && ni.isConnected
                }
}