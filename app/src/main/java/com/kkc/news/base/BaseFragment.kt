package com.kkc.news.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.viewpager2.widget.ViewPager2

abstract class BaseFragment<T : ViewDataBinding>(@LayoutRes private val layoutRes: Int) : Fragment() {
    protected lateinit var binding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiInit()
        disableLoadMoreObserve()
    }

    protected fun setRecyclerViewLoadMore(
        recyclerView: RecyclerView,
        offset: Int,
        callback: () -> Unit
    ) {
        recyclerView.addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == ViewPager2.SCROLL_STATE_IDLE) {
                    recyclerView.adapter?.let {
                        val lastVisiblePosition = (recyclerView.layoutManager as LinearLayoutManager)
                            .findLastCompletelyVisibleItemPosition()
                        val totalCount = it.itemCount - offset

                        if (lastVisiblePosition >= totalCount && it.itemCount != 0) {
                            callback.invoke()
                        }
                    }
                }
            }
        })
    }

    abstract fun uiInit()
    abstract fun disableLoadMoreObserve()
}
