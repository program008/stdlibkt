package com.canbot.stdlibkt.mvvm

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import com.canbot.stdlibkt.bean.Repository
import com.canbot.stdlibkt.mvvm.repo.GitRepoRepository
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by tao.liu on 2018/9/26.
 * description this is description
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
        //constructor(application: Application) : super(application)

        val repoModel: RepoModel = RepoModel()
        val text = ObservableField<String>()
        var isLoading = ObservableField<Boolean>()
        var repositories = MutableLiveData<ArrayList<Repository>>()
        lateinit var disposable: Disposable
        private val compositeDisposable = CompositeDisposable()
        var gitRepoRepository: GitRepoRepository = GitRepoRepository(NetManager(getApplication()))
        fun refresh() {
                isLoading.set(true)
                repoModel.refreshData(object : OnDataReadyCallback {
                        override fun onDataReady(data: String) {
                                isLoading.set(false)
                                text.set(data)
                        }

                })
        }

        fun loadRepositories() {
                isLoading.set(true)
                compositeDisposable += gitRepoRepository
                        .getRepositories()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableObserver<ArrayList<Repository>>() {
                        override fun onError(e: Throwable) {
                                //if some error happens in our data layer our app will not crash, we will
                                // get error here
                        }

                        override fun onNext(data: ArrayList<Repository>) {
                                repositories.value = data
                        }

                        override fun onComplete() {
                                isLoading.set(false)
                        }
                })

        }

        override fun onCleared() {
                super.onCleared()
                if (!compositeDisposable.isDisposed) {
                        compositeDisposable.dispose()
                }
        }

        companion object {
                val TAG = "MainViewModel"
        }
}
