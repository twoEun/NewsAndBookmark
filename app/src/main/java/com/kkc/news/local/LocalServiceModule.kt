package com.kkc.news.local

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.kkc.news.local.service.BookMarkService
import com.kkc.news.local.service.SharedPreferenceService
import com.kkc.news.local.service.SharedPreferenceServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.http.POST

@Module
@InstallIn(SingletonComponent::class)
class LocalServiceModule {
    @Provides
    fun provideSharedPreferenceService(sharedPreferences: SharedPreferences, editor: Editor): SharedPreferenceService {
        return SharedPreferenceServiceImpl(sharedPreferences, editor)
    }

    @Provides
    fun provideBookmarkService(localDatabase: LocalDatabase): BookMarkService {
        return localDatabase.bookmarkService()
    }
}
