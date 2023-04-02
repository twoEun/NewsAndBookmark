package com.kkc.news.network.service

import com.kkc.news.entity.response.NewResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("everything")
    fun newsFromEverything(
        @Query("apiKey") apikey: String,
        @Query("q") keyword: String,
        @Query("sortBy") sortBy: String,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int
    ): Single<NewResponse>

    @GET("top-headlines")
    fun newsFromHeadline(
        @Query("apiKey") apikey: String,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int,
        @Query("category") category: String
    ): Single<NewResponse>
}
