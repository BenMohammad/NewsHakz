package com.benmohammad.newshakz.ui.main

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.benmohammad.newshakz.di.ApplicationComponent
import com.benmohammad.newshakz.di.MainApplication
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseActivity: AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val appComponent: ApplicationComponent
        get() = (application as MainApplication).component

    fun <VM : ViewModel> getViewModel(viewModelClass: KClass<VM>) : VM {
        return ViewModelProviders.of(this, viewModelFactory).get(viewModelClass.java)

    }
}