package com.benmohammad.newshakz.ui.main

import android.app.Application
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(
    app: Application,
    protected val disposables: CompositeDisposable = CompositeDisposable()
) : AndroidViewModel(app), Observable {

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    @Transient
    private var dataBindingCallbacks: PropertyChangeRegistry? = null
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        synchronized(this) {
            if(dataBindingCallbacks == null) {
                dataBindingCallbacks = PropertyChangeRegistry()
            }
        }
        dataBindingCallbacks!!.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        synchronized(this) {
            if(dataBindingCallbacks == null) {
                return
            }
        }

        dataBindingCallbacks!!.remove(callback)
    }

    fun notifyChange() {
        synchronized(this) {
            if(dataBindingCallbacks == null) {
                return
            }
        }
        dataBindingCallbacks!!.notifyCallbacks(this, 0, null)
    }

    fun notifyPropertyChanged(fieldId: Int) {
        synchronized(this) {
            if(dataBindingCallbacks == null) {
                return
            }
        }
        dataBindingCallbacks!!.notifyCallbacks(this, fieldId, null)
    }}