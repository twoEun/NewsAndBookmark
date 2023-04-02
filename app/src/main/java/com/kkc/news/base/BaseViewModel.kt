package com.kkc.news.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
}