package com.kkc.news.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kkc.news.R
import com.kkc.news.databinding.ItemHomeHeaderNewsBinding
import com.kkc.news.entity.data.ArticleData
import com.kkc.news.ui.interfaces.NewsItemClick
import com.kkc.news.util.itemDiff

class HeaderArticlesAdapter(private val newsItemClickListener: NewsItemClick) : ListAdapter<ArticleData, HomeHeaderViewHolder>(itemDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHeaderViewHolder {
        val binding = DataBindingUtil.inflate<ItemHomeHeaderNewsBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_home_header_news,
            parent,
            false
        )

        binding.root.setOnClickListener {
            binding.headerArticle?.let {
                newsItemClickListener.onNewsItemClick(it)
            }
        }

        binding.bookmark.setOnClickListener {
            binding.headerArticle?.let {
                newsItemClickListener.onBookMarkClick(it)
            }
        }

        return HomeHeaderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeHeaderViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}

class HomeHeaderViewHolder(private val binding: ItemHomeHeaderNewsBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(item: ArticleData?) {
        item?.let {
            binding.headerArticle = it
        }
    }
}
