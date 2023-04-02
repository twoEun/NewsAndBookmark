package com.kkc.news.ui.newsDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.kkc.news.base.BaseViewModel
import com.kkc.news.entity.data.ArticleData
import com.kkc.news.local.repository.BookMarkRepository
import com.kkc.news.ui.newsDetail.NewsDetailActivity.Companion.EXTRA_ARTICLE_INFO
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val bookMarkRepository: BookMarkRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    private var article: ArticleData? = null
    private val _bookMarkState = MutableLiveData<Boolean>()
    val bookMarkState: LiveData<Boolean>
        get() = _bookMarkState

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    private val _publishedAt = MutableLiveData<String>()
    val publishedAt: LiveData<String>
        get() = _publishedAt

    private val _source = MutableLiveData<String>()
    val source: LiveData<String>
        get() = _source

    private val _author = MutableLiveData<String>()
    val author: LiveData<String>
        get() = _author

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    private val _newsImage = MutableLiveData<String?>()
    val newsImage: LiveData<String?>
        get() = _newsImage

    private val _newsContent = MutableLiveData<String>()
    val newsContent: LiveData<String>
        get() = _newsContent

    init {
        article = savedStateHandle.get<ArticleData>(EXTRA_ARTICLE_INFO)
        article?.let {
            _bookMarkState.postValue(it.isBookMarked)
            _publishedAt.postValue(dateFormat.format(it.publishedAt))
            _source.postValue(it.articleSource.sourceName)
            _author.postValue(it.author)
            _title.postValue(it.articleTitle)
            _newsImage.postValue(it.articleImage)
            _newsContent.postValue(it.articleContent)
        }
    }

    fun onBookMarkClick() {
        article?.let {
            if (it.isBookMarked) {
                deleteBookMark()
            } else {
                addBookMark()
            }
        }
    }

    private fun deleteBookMark() {
        article?.let {
            bookMarkRepository.deleteBookMark(it.bookmarkIndex)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    it.bookmarkIndex = -1
                    it.isBookMarked = false
                    _bookMarkState.postValue(false)
                }, { err ->
                    err.printStackTrace()
                })
        }
    }

    private fun addBookMark() {
        article?.let {
            bookMarkRepository.addBookMark(it.toBookMark())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    getLastSavedIndex()
                }, { err ->
                    err.printStackTrace()
                })
        }
    }

    private fun getLastSavedIndex() {
        article?.let {
            bookMarkRepository.getLastIndex()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    it.bookmarkIndex = result
                    it.isBookMarked = true
                    _bookMarkState.postValue(true)
                }, { err ->
                    err.printStackTrace()
                })
        }
    }
}
