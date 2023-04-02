package com.kkc.news.ui.bookmark

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.kkc.news.base.BaseViewModel
import com.kkc.news.local.repository.BookMarkRepository
import com.kkc.news.local.roomData.BookMark
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val bookMarkRepository: BookMarkRepository
) : BaseViewModel() {
    private val _bookmarkList = MutableLiveData<List<BookMark>>()
    val bookmarkList: LiveData<List<BookMark>>
        get() = _bookmarkList

    init {
        getBookMarkList()
    }

    @SuppressLint("CheckResult")
    private fun getBookMarkList() {
        bookMarkRepository.getAllBookMarks()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ result ->
                _bookmarkList.postValue(result)
                if (result.isEmpty()) {
                    _noData.postValue(true)
                }
            }, { err ->
                err.printStackTrace()
            })
    }

    fun requestBookMarkDelete(bookMark: BookMark) {
        bookMarkDelete(bookMark)
    }

    @SuppressLint("CheckResult")
    private fun bookMarkDelete(bookMark: BookMark) {
        bookMarkRepository.deleteBookMark(bookMark)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                val tempList = _bookmarkList.value?.toMutableList()
                tempList?.remove(bookMark)
                tempList?.let {
                    _bookmarkList.postValue(it)
                }
            }, { err ->
                err.printStackTrace()
            })
    }
}
