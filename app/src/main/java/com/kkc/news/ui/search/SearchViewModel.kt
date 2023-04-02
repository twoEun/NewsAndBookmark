package com.kkc.news.ui.search

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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val bookMarkRepository: BookMarkRepository,
    private val sharedPreferenceRepository: SharedPreferenceRepository
) : BaseViewModel() {
    var pageNum = 1
    var sizeOfPage = 30
    var currentSortType = SortType.RELEVANCY
    val keyword = MutableLiveData<String>()

    private val _searchClick = MutableLiveData<Event<Unit>>()
    val searchClick: LiveData<Event<Unit>>
        get() = _searchClick

    private val _searchedArticles = MutableLiveData<List<ArticleData>>()
    val searchedArticles: LiveData<List<ArticleData>>
        get() = _searchedArticles

    private val _bookMarks = MutableLiveData<List<BookMark>>()
    val bookMarks: LiveData<List<BookMark>>
        get() = _bookMarks

    private val bookMarkObserver = Observer<List<BookMark>> { bookMarkList ->
        val processedNews = bookMarkCheck(searchedArticles.value ?: emptyList(), bookMarkList)
        _searchedArticles.value = processedNews
    }

    init {
        bookMarks.observeForever(bookMarkObserver)
        getBookMarks()
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
    private fun searchArticles() {
        val keywordEnable = !keyword.value.isNullOrBlank()
        if (pageNum != -1 && keywordEnable) {
            _progressVisible.postValue(true)
            newsRepository.searchArticles(
                keyword.value ?: "",
                currentSortType.typeText,
                pageNum,
                sizeOfPage
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    _progressVisible.postValue(false)
                    if (pageNum == 1 && result.isEmpty()) {
                        _noData.postValue(true)
                    }

                    val tempList = if (pageNum == 1) {
                        mutableListOf()
                    } else {
                        _searchedArticles.value?.toMutableList() ?: mutableListOf()
                    }

                    tempList.addAll(result)
                    val processedList = bookMarkCheck(tempList, bookMarks.value ?: emptyList())
                    _searchedArticles.postValue(processedList)

                    if (result.size == sizeOfPage) {
                        pageNum++
                    } else {
                        pageNum = -1
                    }
                }, { err ->
                    _progressVisible.postValue(false)
                    err.printStackTrace()
                })
        } else {
            if (!_searchedArticles.value.isNullOrEmpty()) {
                _disableLoadMore.postValue(Event(Unit))
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun saveLatestKeyword() {
        sharedPreferenceRepository.setLatestSearchKeyword(keyword.value ?: "")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({}, { err ->
                err.printStackTrace()
            })
    }

    fun onSearchClick() {
        _searchClick.postValue(Event(Unit))
        pageNum = 1
        saveLatestKeyword()
        searchArticles()
    }

    fun onSortChange(selectedPosition: Int) {
        currentSortType = SortType.getSortTypeFromPosition(selectedPosition)
        pageNum = 1
        searchArticles()
    }

    fun requestLoadMore() {
        searchArticles()
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
        val bookMark = BookMark(
            index = 0,
            author = articleData.author,
            title = articleData.articleTitle,
            description = articleData.articleDescription
        )

        bookMarkRepository.addBookMark(bookMark)
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
