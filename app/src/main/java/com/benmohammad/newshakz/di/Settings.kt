package com.benmohammad.newshakz.di

import android.content.Context
import android.text.TextUtils
import com.benmohammad.newshakz.R
import com.benmohammad.newshakz.util.putOrClearPreference

open class Settings(private val context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_SETTINGS, Context.MODE_PRIVATE)

    var baseUrl: String
    get() = preferences.getString(PREFS_BASE_URL, context.getString(R.string.default_base_url))!!
    set(baseUrl) = preferences.putOrClearPreference(
        PREFS_BASE_URL,
        !TextUtils.isEmpty(baseUrl),
        baseUrl
    )

    companion object {
        private const val PREFS_SETTINGS = "settings"
        private const val PREFS_BASE_URL = "base_url"

    }}