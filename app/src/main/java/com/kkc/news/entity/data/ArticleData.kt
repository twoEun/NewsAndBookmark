package com.kkc.news.entity.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.kkc.news.local.roomData.BookMark
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ArticleData(
    @SerializedName("source")
    val articleSource: ArticleSourceData,

    @SerializedName("author")
    val author: String?,

    @SerializedName("title")
    val articleTitle: String,

    @SerializedName("description")
    val articleDescription: String?,

    @SerializedName("url")
    val articleUrl: String,

    @SerializedName("urlToImage")
    val articleImage: String?,

    @SerializedName("publishedAt")
    val publishedAt: Date,

    @SerializedName("content")
    val articleContent: String?,

    var isBookMarked: Boolean = false,

    var bookmarkIndex: Int = -1
) : Parcelable {
    fun toBookMark(): BookMark {
        return BookMark(
            0,
            title = articleTitle,
            author = author,
            description = articleDescription
        )
    }
}
