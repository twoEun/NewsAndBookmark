package com.kkc.news.ui.bookmark

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kkc.news.R
import com.kkc.news.base.BaseFragment
import com.kkc.news.databinding.FragmentBookmarkBinding
import com.kkc.news.local.roomData.BookMark
import com.kkc.news.ui.adapters.BookMarkAdapter
import com.kkc.news.ui.interfaces.BookMarkItemClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : BaseFragment<FragmentBookmarkBinding>(R.layout.fragment_bookmark), BookMarkItemClick {
    private val viewModel by viewModels<BookmarkViewModel>()
    private val adapter = BookMarkAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        bookMarkListObserve()
    }

    override fun uiInit() {
        binding.bookMarkList.adapter = adapter
    }

    private fun bookMarkListObserve() {
        val bookMarkListObserver = Observer<List<BookMark>> { bookMarkList ->
            adapter.submitList(bookMarkList)
        }

        viewModel.bookmarkList.observe(viewLifecycleOwner, bookMarkListObserver)
    }

    override fun disableLoadMoreObserve() {
    }

    override fun onBookMarkItemClick(bookMark: BookMark) {
        viewModel.requestBookMarkDelete(bookMark)
    }
}
