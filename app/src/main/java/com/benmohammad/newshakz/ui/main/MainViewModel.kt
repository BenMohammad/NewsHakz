package com.benmohammad.newshakz.ui.main

import android.app.Application
import com.benmohammad.newshakz.data.network.HackerNewsInteractor
import javax.inject.Inject

class MainViewModel @Inject constructor(
    app: Application,
    private val hackerNewsInteractor: HackerNewsInteractor
) : BaseViewModel(app){


}