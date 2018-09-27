package com.canbot.stdlibkt.mvvm

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import com.canbot.stdlibkt.bean.Repository
import com.canbot.stdlibkt.mvvm.repo.GitRepoRepository
import com.canbot.stdlibkt.mvvm.repo.OnRepositoryReadyCallback

/**
 * Created by tao.liu on 2018/9/26.
 * description this is description
 */
class MainViewModel : AndroidViewModel {
        constructor(application: Application) : super(application)
        //val repoModel:RepoModel = RepoModel()
        val text = ObservableField<String>()
        var isLoading = ObservableField<Boolean>()
        var repositories = MutableLiveData<ArrayList<Repository>>()

        var gitRepoRepository: GitRepoRepository = GitRepoRepository(NetManager(getApplication()))
        fun refresh(){
               /* isLoading.set(true)
                repoModel.refreshData(object :OnDataReadyCallback{
                        override fun onDataReady(data: String) {
                                isLoading.set(false)
                                text.set(data)
                        }

                })*/
        }

        fun loadRepositories(){
               /* Log.e(TAG,"加载数据loadRepositories()")
                isLoading.set(true)
                repoModel.getRepositories(object : OnRepositoryReadyCallback{
                        override fun onDataReady(data: ArrayList<Repository>) {
                                isLoading.set(false)
                                repositories.value = data
                                Log.e(TAG,"加载成功$repositories")
                        }
                })*/
                isLoading.set(true)
                gitRepoRepository.getRepositories(object : OnRepositoryReadyCallback {
                        override fun onDataReady(data: ArrayList<Repository>) {
                                isLoading.set(false)
                                repositories.value = data
                        }
                })
        }

        companion object {
                val TAG = "MainViewModel"
        }
}