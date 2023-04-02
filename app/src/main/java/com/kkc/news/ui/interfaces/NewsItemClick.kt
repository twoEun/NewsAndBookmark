package com.kkc.news.ui.interfaces

import com.kkc.news.entity.data.ArticleData

interface NewsItemClick {
    fun onNewsItemClick(articleData: ArticleData)
    fun onBookMarkClick(articleData: ArticleData)
}
