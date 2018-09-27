package com.canbot.stdlibkt.mvvm.repo

import com.canbot.stdlibkt.bean.Repository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class GitRepoLocalDataSource {

        fun getRepositories(): Observable<ArrayList<Repository>> {
                val arrayList = ArrayList<Repository>()
                arrayList.add(Repository("First From Local", "Owner 1", 100, false))
                arrayList.add(Repository("Second From Local", "Owner 2", 30, true))
                arrayList.add(Repository("Third From Local", "Owner 3", 430, false))

                //Handler().postDelayed({ onRepositoryReadyCallback.onLocalDataReady(arrayList) }, 2000)
                return Observable.just(arrayList).delay(2, TimeUnit.SECONDS)
        }

        fun saveRepositories(arrayList: ArrayList<Repository>):Completable {
                return Single.just(1).delay(1,TimeUnit.SECONDS).toCompletable()
        }
}

/*
interface OnRepoLocalReadyCallback {
    fun onLocalDataReady(data: ArrayList<Repository>)
}*/
