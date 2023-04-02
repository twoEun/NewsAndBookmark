package com.kkc.news.ui.home

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.kkc.news.base.BaseViewModel
import com.kkc.news.entity.data.ArticleData
import com.kkc.news.enum.SortType
import com.kkc.news.local.repository.BookMarkRepository
import com.kkc.news.local.repository.SharedPreferenceRepository
import com.kkc.news.local.roomData.BookMark
import com.kkc.news.network.repository.NewsRepository
import com.kkc.news.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

@SuppressLint("CheckResult")
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val sharedPreferenceRepository: SharedPreferenceRepository,
    private val bookMarkRepository: BookMarkRepository
) : BaseViewModel() {
    private var recentPageNum = 1
    private var headLinePageNum = 1
    private val sizeOfPage = 30

    private val _recentArticles = MutableLiveData<List<ArticleData>>()
    val recentArticles: LiveData<List<ArticleData>>
        get() = _recentArticles

    private val _headlineArticles = MutableLiveData<List<ArticleData>>()
    val headlineArticles: LiveData<List<ArticleData>>
        get() = _headlineArticles

    private val _bookMarks = MutableLiveData<List<BookMark>>()
    val bookMarks: LiveData<List<BookMark>>
        get() = _bookMarks

    private val bookMarkObserver = Observer<List<BookMark>> { bookMarkList ->
        val tempHeaderNews = recentArticles.value ?: emptyList()
        val tempHeadlines = headlineArticles.value ?: emptyList()

        val processedHeader = tempHeaderNews.map { article ->
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

        val processedHeadLine = tempHeadlines.map { article ->
            val headlineBookmark = bookMarkList.find { bookmark ->
                article.author == bookmark.author &&
                    article.articleTitle == bookmark.title &&
                    article.articleDescription == bookmark.description
            }

            if (headlineBookmark != null) {
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
                    bookmarkIndex = headlineBookmark.index
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

        _recentArticles.value = processedHeader
        _headlineArticles.value = processedHeadLine
    }

    init {
        bookMarks.observeForever(bookMarkObserver)
        requestFirstData()
    }

    private fun requestFirstData() {
        val latestKeyword = sharedPreferenceRepository.getLatestSearchKeyword()
        Single.zip(
            if (latestKeyword.isNullOrBlank()) {
                Single.just(emptyList())
            } else {
                newsRepository.searchArticles(
                    latestKeyword,
                    SortType.RELEVANCY.typeText,
                    recentPageNum,
                    sizeOfPage
                )
            },
            newsRepository.getHeadLineArticles(
                headLinePageNum,
                sizeOfPage,
                "general"
            )
        ) { recentNews, headlineNews ->
            if (recentPageNum == 1 && recentNews.isEmpty()) {
                _noData.postValue(true)
            }

            val tempList = recentArticles.value?.toMutableList() ?: mutableListOf()
            tempList.addAll(recentNews)
            _recentArticles.postValue(tempList)

            recentPageNum = if (recentNews.size == sizeOfPage) {
                recentPageNum++
            } else {
                -1
            }

            if (headLinePageNum == 1 && headlineNews.isEmpty()) {
                _noData.postValue(true)
            }

            val tempHeadLineList = _headlineArticles.value?.toMutableList() ?: mutableListOf()
            tempHeadLineList.addAll(headlineNews)
            _headlineArticles.postValue(tempHeadLineList)

            headLinePageNum = if (headlineNews.size == sizeOfPage) {
                headLinePageNum++
            } else {
                -1
            }
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doFinally {
                getBookMarks()
            }
            .subscribe({}, { err ->
                err.printStackTrace()
            })
    }

    @SuppressLint("CheckResult")
    private fun getBookMarks() {
        bookMarkRepository.getAllBookMarks()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ result ->
                _bookMarks.postValue(result)
            }, { err ->
                err.printStackTrace()
            })
    }

    @SuppressLint("CheckResult")
    private fun getRecentArticles() {
        if (recentPageNum != -1) {
            val latestKeyword = sharedPreferenceRepository.getLatestSearchKeyword()
            newsRepository.searchArticles(
                latestKeyword,
                SortType.RELEVANCY.typeText,
                recentPageNum,
                sizeOfPage
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    if (recentPageNum == 1 && result.isEmpty()) {
                        _noData.postValue(true)
                    }

                    val tempList = recentArticles.value?.toMutableList() ?: mutableListOf()
                    tempList.addAll(result)
                    _recentArticles.postValue(tempList)

                    recentPageNum = if (result.size == sizeOfPage) {
                        recentPageNum++
                    } else {
                        -1
                    }
                }, { err ->
                    err.printStackTrace()
                })
        } else {
            _disableLoadMore.postValue(Event(Unit))
        }
    }

    @SuppressLint("CheckResult")
    private fun getHeadLineArticles() {
        if (headLinePageNum != -1) {
            newsRepository.getHeadLineArticles(
                headLinePageNum,
                sizeOfPage,
                "general"
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    if (headLinePageNum == 1 && result.isEmpty()) {
                        _noData.postValue(true)
                    }

                    val tempHeadLineList = _headlineArticles.value?.toMutableList() ?: mutableListOf()
                    tempHeadLineList.addAll(result)
                    _headlineArticles.postValue(tempHeadLineList)

                    headLinePageNum = if (result.size == sizeOfPage) {
                        headLinePageNum++
                    } else {
                        -1
                    }
                }, { err ->
                    err.printStackTrace()
                })
        } else {
            _disableLoadMore.postValue(Event(Unit))
        }
    }

    fun requestHeaderArticleLoadMore() {
        getRecentArticles()
    }

    fun requestHeadlineArticleLoadMore() {
        getHeadLineArticles()
    }

    fun requestBookMarkProcess(clickedArticle: ArticleData) {
        if (clickedArticle.isBookMarked) {
            removeFromBookMark(clickedArticle)
        } else {
            addBookMark(clickedArticle)
        }
    }

    @SuppressLint("CheckResult")
    private fun addBookMark(articleData: ArticleData) {
        bookMarkRepository.addBookMark(articleData.toBookMark())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
            }, { err ->
                err.printStackTrace()
            })
    }

    @SuppressLint("CheckResult")
    private fun removeFromBookMark(articleData: ArticleData) {
        bookMarkRepository.deleteBookMark(articleData.bookmarkIndex)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
            }, { err ->
                err.printStackTrace()
            })
    }

    override fun onCleared() {
        super.onCleared()
        bookMarks.removeObserver(bookMarkObserver)
    }
}
