package com.kkc.news.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kkc.news.R
import com.kkc.news.databinding.ItemNewsBinding
import com.kkc.news.entity.data.ArticleData
import com.kkc.news.ui.interfaces.NewsItemClick
import com.kkc.news.util.itemDiff

class NewsAdapter(private val newsItemClickListener: NewsItemClick) : ListAdapter<ArticleData, NewsViewHolder>(itemDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = DataBindingUtil.inflate<ItemNewsBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_news,
            parent,
            false
        )

        binding.root.setOnClickListener {
            binding.news?.let {
                newsItemClickListener.onNewsItemClick(it)
            }
        }

        binding.bookMarkState.setOnClickListener {
            binding.news?.let {
                newsItemClickListener.onBookMarkClick(it)
            }
        }

        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}

class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(item: ArticleData?) {
        item?.let {
            binding.news = it
        }
    }
}
