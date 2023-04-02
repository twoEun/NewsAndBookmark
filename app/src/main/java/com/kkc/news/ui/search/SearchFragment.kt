package com.kkc.news.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kkc.news.R
import com.kkc.news.base.BaseFragment
import com.kkc.news.databinding.FragmentSearchBinding
import com.kkc.news.entity.data.ArticleData
import com.kkc.news.ui.adapters.NewsAdapter
import com.kkc.news.ui.interfaces.NewsItemClick
import com.kkc.news.ui.newsDetail.NewsDetailActivity
import com.kkc.news.util.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search), NewsItemClick {
    private val viewModel by viewModels<SearchViewModel>()
    private val adapter = NewsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.searchInfo = viewModel

        searchClickObserve()
        searchedArticlesObserve()
    }

    override fun uiInit() {
        binding.searchedArticles.adapter = adapter
        setRecyclerViewLoadMore(binding.searchedArticles, 5) {
            viewModel.requestLoadMore()
        }
    }

    private fun searchClickObserve() {
        val searchClickObserver = EventObserver<Unit> {
            hideKeyBoard()
        }
        viewModel.searchClick.observe(viewLifecycleOwner, searchClickObserver)
    }

    private fun searchedArticlesObserve() {
        val searchedArticlesObserver = Observer<List<ArticleData>> { searchedArticles ->
            adapter.submitList(searchedArticles)
        }
        viewModel.searchedArticles.observe(viewLifecycleOwner, searchedArticlesObserver)
    }

    @SuppressLint("ServiceCast")
    private fun hideKeyBoard() {
        val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputManager?.also {
            it.hideSoftInputFromWindow(binding.keyword.windowToken, 0)
        }
    }

    override fun disableLoadMoreObserve() {
        val disableLoadMoreObserver = EventObserver<Unit> {
            context?.let {
                Toast.makeText(it, R.string.disable_loadMore, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.disableLoadMore.observe(viewLifecycleOwner, disableLoadMoreObserver)
    }

    override fun onNewsItemClick(articleData: ArticleData) {
        context?.let {
            startActivity(NewsDetailActivity.newsDetailIntent(it, articleData))
        }
    }

    override fun onBookMarkClick(articleData: ArticleData) {
        viewModel.requestBookMarkProcess(articleData)
    }
}
