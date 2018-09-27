package com.canbot.stdlibkt.mvvm

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.canbot.stdlibkt.R
import com.canbot.stdlibkt.databinding.ActivityMain2Binding
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {
        lateinit var binding: ActivityMain2Binding
        //val  viewModel = MainViewModel()
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                binding = DataBindingUtil.setContentView(this,R.layout.activity_main2)
                binding.viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
                binding.executePendingBindings()
        }
}
