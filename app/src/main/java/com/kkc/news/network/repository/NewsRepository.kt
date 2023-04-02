package com.kkc.news.network.repository

import com.kkc.news.entity.data.ArticleData
import com.kkc.news.network.service.NewsService
import io.reactivex.Single
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsService: NewsService
) : NewsRepository {
    private val apiKey = "9aafaa6c8c294e47b06b825bd1de0e08"

    override fun searchArticles(
        query: String,
        sortBy: String,
        page: Int,
        pageSize: Int
    ): Single<List<ArticleData>> {
        return try {
            val search = newsService.newsFromEverything(apiKey, query, sortBy, page = page, pageSize = pageSize)
            search.map { searched ->
                if (searched.status == "ok") {
                    searched.articles ?: emptyList()
                } else {
                    emptyList()
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            Single.just(emptyList())
        }
    }

    override fun getHeadLineArticles(
        page: Int,
        pageSize: Int,
        category: String
    ): Single<List<ArticleData>> {
        return try {
            val headlineArticles = newsService.newsFromHeadline(apiKey, page = page, pageSize = pageSize, category = category)
            headlineArticles.map { headline ->
                if (headline.status == "ok") {
                    headline.articles ?: emptyList()
                } else {
                    emptyList()
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            Single.just(emptyList())
        }
    }
}

interface NewsRepository {
    fun searchArticles(query: String, sortBy: String, page: Int, pageSize: Int): Single<List<ArticleData>>
    fun getHeadLineArticles(page: Int = 1, pageSize: Int, category: String): Single<List<ArticleData>>
}
