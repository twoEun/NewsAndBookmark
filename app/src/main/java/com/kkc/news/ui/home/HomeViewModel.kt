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
        val processedHeader = bookMarkCheck(recentArticles.value ?: emptyList(), bookMarkList)
        val processedHeadLine = bookMarkCheck(headlineArticles.value ?: emptyList(), bookMarkList)

        _recentArticles.value = processedHeader
        _headlineArticles.value = processedHeadLine
    }

    init {
        bookMarks.observeForever(bookMarkObserver)
        requestFirstData()
    }

    private fun requestFirstData() {
        _progressVisible.postValue(true)
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
            _progressVisible.postValue(false)
            if (recentPageNum == 1 && recentNews.isEmpty()) {
                _noData.postValue(true)
            }

            val tempList = recentArticles.value?.toMutableList() ?: mutableListOf()
            tempList.addAll(recentNews)
            _recentArticles.postValue(tempList)

            if (recentNews.size == sizeOfPage) {
                recentPageNum++
            } else {
                recentPageNum = -1
            }

            if (headLinePageNum == 1 && headlineNews.isEmpty()) {
                _noData.postValue(true)
            }

            val tempHeadLineList = _headlineArticles.value?.toMutableList() ?: mutableListOf()
            tempHeadLineList.addAll(headlineNews)
            _headlineArticles.postValue(tempHeadLineList)

            if (headlineNews.size == sizeOfPage) {
                headLinePageNum++
            } else {
                headLinePageNum = -1
            }
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doFinally {
                getBookMarks()
            }
            .subscribe({}, { err ->
                _progressVisible.postValue(false)
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
            _progressVisible.postValue(true)
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
                    _progressVisible.postValue(false)
                    if (recentPageNum == 1 && result.isEmpty()) {
                        _noData.postValue(true)
                    }

                    val tempList = recentArticles.value?.toMutableList() ?: mutableListOf()
                    tempList.addAll(result)
                    _recentArticles.postValue(tempList)

                    if (result.size == sizeOfPage) {
                        recentPageNum++
                    } else {
                        recentPageNum = -1
                    }
                }, { err ->
                    _progressVisible.postValue(false)
                    err.printStackTrace()
                })
        } else {
            _disableLoadMore.postValue(Event(Unit))
        }
    }

    @SuppressLint("CheckResult")
    private fun getHeadLineArticles() {
        if (headLinePageNum != -1) {
            _progressVisible.postValue(true)
            newsRepository.getHeadLineArticles(
                headLinePageNum,
                sizeOfPage,
                "general"
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    _progressVisible.postValue(false)
                    if (headLinePageNum == 1 && result.isEmpty()) {
                        _noData.postValue(true)
                    }

                    val tempHeadLineList = _headlineArticles.value?.toMutableList() ?: mutableListOf()
                    tempHeadLineList.addAll(result)
                    _headlineArticles.postValue(tempHeadLineList)

                    if (result.size == sizeOfPage) {
                        headLinePageNum++
                    } else {
                        headLinePageNum = -1
                    }
                }, { err ->
                    _progressVisible.postValue(false)
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
