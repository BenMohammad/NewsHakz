package com.benmohammad.newshakz.ui

import android.text.method.SingleLineTransformationMethod
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

class NavigationEvent<T>: SingleLiveEvent<T>() {

    fun observe(owner: LifecycleOwner, observer: NavigationObserver<T>) {
        super.observe(owner, Observer {
            event -> if(event == null) {

            return@Observer
        }
            observer.onNavigationEvent(event)
        }
        )
    }


    interface NavigationObserver<in T> {
        fun onNavigationEvent(event: T)
    }
}