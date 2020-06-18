package com.benmohammad.newshakz.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideSettings(context: Context) = Settings(context)
}