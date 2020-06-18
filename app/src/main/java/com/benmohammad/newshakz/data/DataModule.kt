package com.benmohammad.newshakz.data

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.benmohammad.newshakz.data.network.HackerNewsApiService
import com.benmohammad.newshakz.data.network.HackerNewsInteractor
import com.benmohammad.newshakz.data.room.ItemDatabase
import com.benmohammad.newshakz.data.room.ItemDatabase.DataBaseMigrations
import com.benmohammad.newshakz.data.room.ItemStore
import com.benmohammad.newshakz.di.Settings
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideCache(app: Application): Cache {
        val cacheDir =  File(app.cacheDir, "http" )
        return Cache(cacheDir, DISK_CACHE_SIZE.toLong())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(cache: Cache): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.cache(cache)
        return builder.build()
    }


    @Singleton
    @Provides
    fun provideBaseUrl(settings: Settings): String {
        return settings.baseUrl
    }

    @Singleton
    @Provides
    fun provideConverter(): Converter.Factory {
        val moshi = Moshi.Builder().build()
        return MoshiConverterFactory.create(moshi)
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        baseUrl: String,
        converterFactory: Converter.Factory
    ): Retrofit {

        return Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideHackerNewsService(
        context: Context,
        api: HackerNewsApiService,
        itemStore: ItemStore
        ): HackerNewsInteractor {
        return HackerNewsInteractor(context, api, itemStore)
    }

    @Singleton
    @Provides
    fun provideHackerNewsApiService(retrofit: Retrofit): HackerNewsApiService {
        return retrofit.create(HackerNewsApiService::class.java)

    }

    @Singleton
    @Provides
    fun provideDatabase(context: Context) =
        Room.databaseBuilder(context, ItemDatabase::class.java, "Items")
            .apply { DataBaseMigrations.migrations.forEach { addMigrations(it) } }
            .build()

    @Singleton
    @Provides
    fun provideItemStore(database: ItemDatabase): ItemStore {
        return ItemStore(database)
    }



    companion object {
        private const val DISK_CACHE_SIZE = 50 * 1024 * 1024
    }
}