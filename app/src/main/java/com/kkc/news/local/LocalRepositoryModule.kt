package com.kkc.news.local

import com.kkc.news.local.repository.BookMarkRepository
import com.kkc.news.local.repository.BookMarkRepositoryImpl
import com.kkc.news.local.repository.SharedPreferenceRepository
import com.kkc.news.local.repository.SharedPreferenceRepositoryImpl
import com.kkc.news.local.service.BookMarkService
import com.kkc.news.local.service.SharedPreferenceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalRepositoryModule {
    @Provides
    @Singleton
    fun provideSharedPreferenceRepository(sharedPreferenceService: SharedPreferenceService): SharedPreferenceRepository {
        return SharedPreferenceRepositoryImpl(sharedPreferenceService)
    }

    @Provides
    @Singleton
    fun provideBookmarkRepository(bookMarkService: BookMarkService) : BookMarkRepository {
        return BookMarkRepositoryImpl(bookMarkService)
    }
}
