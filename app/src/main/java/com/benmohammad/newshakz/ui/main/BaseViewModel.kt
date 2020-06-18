package com.benmohammad.newshakz.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable



abstract class BaseViewModel(
    app: Application,
    protected val disposables: CompositeDisposable = CompositeDisposable()
) : AndroidViewModel(app) {

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }


}