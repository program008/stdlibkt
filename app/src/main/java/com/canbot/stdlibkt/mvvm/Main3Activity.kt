package com.canbot.stdlibkt.mvvm

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.canbot.stdlibkt.R
import com.canbot.stdlibkt.databinding.ActivityMain3Binding

class Main3Activity : AppCompatActivity() ,RepositoryRecyclerViewAdapter.OnItemClickListener {

        lateinit var binding: ActivityMain3Binding
        private val repositoryRecyclerViewAdapter = RepositoryRecyclerViewAdapter(arrayListOf(), this)
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                //setContentView(R.layout.activity_main3)
                binding = DataBindingUtil.setContentView(this, R.layout.activity_main3)
                var viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
                binding.viewModel = viewModel
                binding.executePendingBindings()

                binding.repositoryRv.layoutManager = LinearLayoutManager(this)
                binding.repositoryRv.adapter = repositoryRecyclerViewAdapter
                viewModel.repositories.observe(this, Observer { it?.let { repositoryRecyclerViewAdapter.replaceData(it) } })

        }

        override fun onItemClick(position: Int) {
                Toast.makeText(this, "您点击的是$position", Toast.LENGTH_SHORT).show()
        }
}
