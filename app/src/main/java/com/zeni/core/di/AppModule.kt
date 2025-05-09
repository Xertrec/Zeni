package com.zeni.core.di

import android.content.Context
import android.content.SharedPreferences
import com.zeni.BuildConfig
import com.zeni.core.data.SharedPrefsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext appContext: Context
    ): SharedPreferences =
        appContext.getSharedPreferences("${BuildConfig.APPLICATION_ID}_preferences", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideSharedPrefsManager(
        @ApplicationContext appContext: Context,
        sharedPreferences: SharedPreferences
    ): SharedPrefsManager = SharedPrefsManager(appContext, sharedPreferences)
}
