package com.kkc.news.entity.response

import com.google.gson.annotations.SerializedName
import com.kkc.news.entity.data.ArticleData

open class NewResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("totalResults")
    val totalResultCount: Int?,

    @SerializedName("articles")
    val articles: List<ArticleData>?,

    @SerializedName("message")
    val errorMessage: String?
)
