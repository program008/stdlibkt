package com.canbot.stdlibkt.mvvm

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by tao.liu on 2018/9/27.
 * description this is description
 */
operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
        add(disposable)
}