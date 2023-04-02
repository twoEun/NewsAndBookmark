package com.kkc.news.ui.newsDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kkc.news.R
import com.kkc.news.databinding.ActivityNewsDetailBinding
import com.kkc.news.entity.data.ArticleData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ARTICLE_INFO = "article_info"
        fun newsDetailIntent(context: Context, articleData: ArticleData): Intent {
            val intent = Intent(context, NewsDetailActivity::class.java)
            intent.putExtra(EXTRA_ARTICLE_INFO, articleData)

            return intent
        }
    }

    private lateinit var binding: ActivityNewsDetailBinding
    private val viewModel by viewModels<NewsDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail)
        binding.lifecycleOwner = this
        binding.newsDetail = viewModel
    }
}
