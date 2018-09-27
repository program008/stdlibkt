package com.canbot.stdlibkt.mvvm.repo

import android.util.Log
import com.canbot.stdlibkt.bean.Repository
import com.canbot.stdlibkt.mvvm.NetManager
import io.reactivex.Observable

class GitRepoRepository(val netManager: NetManager) {

        val localDataSource = GitRepoLocalDataSource()
        val remoteDataSource = GitRepoRemoteDataSource()

        fun getRepositories(): Observable<ArrayList<Repository>> {

                netManager.isConnectedToInternet?.let {
                        if (it) {
                                return remoteDataSource.getRepositories().flatMap {
                                        return@flatMap localDataSource.saveRepositories(it)
                                                .toSingleDefault(it)
                                                .toObservable()
                                }
                        }
                }
                return localDataSource.getRepositories()
        }
}

/*
interface OnRepositoryReadyCallback {
        fun onDataReady(data: ArrayList<Repository>)
}*/
