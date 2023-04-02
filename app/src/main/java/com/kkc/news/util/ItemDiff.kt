package com.kkc.news.util

import androidx.recyclerview.widget.DiffUtil
import com.kkc.news.entity.data.ArticleData

val itemDiff = object : DiffUtil.ItemCallback<ArticleData>() {
    override fun areItemsTheSame(oldItem: ArticleData, newItem: ArticleData): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ArticleData, newItem: ArticleData): Boolean {
        return oldItem.isBookMarked == newItem.isBookMarked && oldItem.bookmarkIndex == newItem.bookmarkIndex
    }
}
