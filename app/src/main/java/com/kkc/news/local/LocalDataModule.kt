package com.kkc.news.local

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {
    private val SHARED_MEMORY_KEY = "sharedPreference"

    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_MEMORY_KEY, Context.MODE_PRIVATE)
    }

    @Provides
    fun provideSharedPreferenceEditor(sharedPreferences: SharedPreferences): Editor {
        return sharedPreferences.edit()
    }

    @Provides
    fun provideLocalDataBase(@ApplicationContext context: Context): LocalDatabase {
        return LocalDatabase.getDatabase(context = context)
    }
}
