package com.canbot.stdlibkt

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.canbot.stdlibkt.bean.Repository
import com.canbot.stdlibkt.commonshapebtn.CommonShapeButtonActivity
import com.canbot.stdlibkt.databinding.ActivityMainBinding
import com.canbot.stdlibkt.mvvm.Main2Activity
import com.canbot.stdlibkt.mvvm.Main3Activity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
        lateinit var binding: ActivityMainBinding
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)
                binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
                //binding.textView.text = "hello data bindings"
                val repository = Repository("Medium Android Repository Article",
                        "Mladen Rakonjac", 1000, true)
                binding.repository = repository

                Handler().postDelayed({repository.repositoryName="New Name"
                        binding.repository = repository
                        binding.executePendingBindings()}, 2000)

                button.setOnClickListener { view -> startActivity(Intent(this, Main3Activity::class.java)) }
                common.setOnClickListener { view -> startActivity(Intent(this,CommonShapeButtonActivity::class.java)) }
        }
}
