package com.kkc.news.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import com.kkc.news.R
import com.kkc.news.base.BaseFragment
import com.kkc.news.databinding.FragmentHomeBinding
import com.kkc.news.entity.data.ArticleData
import com.kkc.news.ui.adapters.HeaderArticlesAdapter
import com.kkc.news.ui.adapters.NewsAdapter
import com.kkc.news.ui.interfaces.NewsItemClick
import com.kkc.news.ui.newsDetail.NewsDetailActivity
import com.kkc.news.ui.newsDetail.NewsDetailActivity.Companion.EXTRA_ARTICLE_INFO
import com.kkc.news.util.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home), NewsItemClick {
    private val viewModel by viewModels<HomeViewModel>()
    private val headerAdapter = HeaderArticlesAdapter(this)
    private val headLineAdapter = NewsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.newsInfo = viewModel

        recentArticlesObserve()
        headlineArticlesObserve()
        disableLoadMoreObserve()
    }

    override fun uiInit() {
        binding.relativeArticles.adapter = headerAdapter
        binding.relativeArticles.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == SCROLL_STATE_IDLE) {
                    val lastVisiblePosition = (recyclerView.layoutManager as LinearLayoutManager)
                        .findLastCompletelyVisibleItemPosition()
                    val totalCount = headerAdapter.itemCount - 5

                    if (lastVisiblePosition >= totalCount && headerAdapter.itemCount != 0) {
                        viewModel.requestHeaderArticleLoadMore()
                    }
                }
            }
        })

        binding.headlineArticles.adapter = headLineAdapter
        binding.headlineArticles.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_IDLE) {
                    val lastVisiblePosition = (recyclerView.layoutManager as LinearLayoutManager)
                        .findLastCompletelyVisibleItemPosition()
                    val totalCount = headLineAdapter.itemCount - 5

                    if (lastVisiblePosition >= totalCount && headLineAdapter.itemCount != 0) {
                        viewModel.requestHeadlineArticleLoadMore()
                    }
                }
            }
        })
    }

    private fun recentArticlesObserve() {
        val recentArticlesObserver = Observer<List<ArticleData>> { recentArticles ->
            headerAdapter.submitList(recentArticles)
        }
        viewModel.recentArticles.observe(viewLifecycleOwner, recentArticlesObserver)
    }

    private fun headlineArticlesObserve() {
        val headlineArticlesObserver = Observer<List<ArticleData>> { headlineArticles ->
            headLineAdapter.submitList(headlineArticles)
        }
        viewModel.headlineArticles.observe(viewLifecycleOwner, headlineArticlesObserver)
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
            val intent = Intent(it, NewsDetailActivity::class.java)
            intent.putExtra(EXTRA_ARTICLE_INFO, articleData)
            startActivity(intent)
        }
    }

    override fun onBookMarkClick(articleData: ArticleData) {
        viewModel.requestBookMarkProcess(articleData)
    }
}
