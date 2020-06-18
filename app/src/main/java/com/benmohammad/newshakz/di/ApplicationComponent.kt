package com.benmohammad.newshakz.di

import android.app.ActivityManager
import com.benmohammad.newshakz.MainActivity
import com.benmohammad.newshakz.data.DataModule
import com.benmohammad.newshakz.ui.main.ViewModelFactoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ AndroidModule::class,
                AppModule::class,
                DataModule::class,
                ViewModelFactoryModule::class]
)
interface ApplicationComponent {

    fun inject(application: MainApplication)

    fun inject(activityManager: MainActivity)
}