package com.kkc.news.network

import com.kkc.news.network.repository.NewsRepository
import com.kkc.news.network.repository.NewsRepositoryImpl
import com.kkc.news.network.service.NewsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    fun provideNewsRepository(searchService: NewsService): NewsRepository {
        return NewsRepositoryImpl(searchService)
    }
}
