package com.kkc.news.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kkc.news.entity.data.ArticleData
import com.kkc.news.local.roomData.BookMark
import com.kkc.news.util.Event

open class BaseViewModel : ViewModel() {
    protected val _noData = MutableLiveData(false)
    val noData: LiveData<Boolean>
        get() = _noData

    protected val _disableLoadMore = MutableLiveData<Event<Unit>>()
    val disableLoadMore: LiveData<Event<Unit>>
        get() = _disableLoadMore

    protected val _progressVisible = MutableLiveData(false)
    val progressVisible: LiveData<Boolean>
        get() = _progressVisible

    protected fun bookMarkCheck(originList: List<ArticleData>, bookMarkList: List<BookMark>): List<ArticleData> {
        return originList.map { article ->
            val headerBookmark = bookMarkList.find { bookmark ->
                article.author == bookmark.author &&
                        article.articleTitle == bookmark.title &&
                        article.articleDescription == bookmark.description
            }

            if (headerBookmark != null) {
                ArticleData(
                    articleSource = article.articleSource,
                    author = article.author,
                    articleTitle = article.articleTitle,
                    articleDescription = article.articleDescription,
                    articleUrl = article.articleUrl,
                    articleImage = article.articleImage,
                    publishedAt = article.publishedAt,
                    articleContent = article.articleContent,
                    isBookMarked = true,
                    bookmarkIndex = headerBookmark.index
                )
            } else {
                if (article.isBookMarked) {
                    ArticleData(
                        articleSource = article.articleSource,
                        author = article.author,
                        articleTitle = article.articleTitle,
                        articleDescription = article.articleDescription,
                        articleUrl = article.articleUrl,
                        articleImage = article.articleImage,
                        publishedAt = article.publishedAt,
                        articleContent = article.articleContent,
                        isBookMarked = false,
                        bookmarkIndex = -1
                    )
                } else {
                    article
                }
            }
        }
    }
}