package com.canbot.stdlibkt.mvvm.repo

import com.canbot.stdlibkt.bean.Repository
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class GitRepoRemoteDataSource {

        fun getRepositories(): Observable<ArrayList<Repository>> {
                val arrayList = ArrayList<Repository>()
                arrayList.add(Repository("First from remote", "Owner 1", 100, false))
                arrayList.add(Repository("Second from remote", "Owner 2", 30, true))
                arrayList.add(Repository("Third from remote", "Owner 3", 430, false))

                //Handler().postDelayed({ onRepositoryReadyCallback.onRemoteDataReady(arrayList) }, 2000)
                return Observable.just(arrayList).delay(2, TimeUnit.SECONDS)

        }
}

/*
interface OnRepoRemoteReadyCallback {
    fun onRemoteDataReady(data: ArrayList<Repository>)
}*/
