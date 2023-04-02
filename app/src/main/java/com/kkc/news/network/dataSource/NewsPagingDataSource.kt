/*
package com.kkc.news.network.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kkc.news.entity.data.ArticleData
import com.kkc.news.network.service.NewsService
import okio.IOException
import retrofit2.HttpException

class NewsPagingDataSource(
    private val newsService: NewsService,
    private val apiKey: String,
    private val query: String,
    private val sortBy: String = "publishedAt"
) : PagingSource<Int, ArticleData>() {
    override fun getRefreshKey(state: PagingState<Int, ArticleData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleData> {
        val pageNum = params.key ?: 1
        val newsSearch = newsService.newsFromEverything(apiKey, query, sortBy = sortBy, page = pageNum, pageSize = params.loadSize)

        return try {
            var nextKey = if ((newsSearch.articles?.size ?: 0) < params.loadSize) {
                null
            } else {
                pageNum + 1
            }

            LoadResult.Page(
                data = newsSearch.articles ?: emptyList(),
                prevKey = if (pageNum == 1) null else pageNum - 1,
                nextKey = nextKey
            )
        } catch (ioException: IOException) {
            return LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        } catch (httpException: HttpException) {
            return LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        }
    }
}
*/
