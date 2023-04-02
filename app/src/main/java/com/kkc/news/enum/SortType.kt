package com.kkc.news.enum

enum class SortType(val typeText: String) {
    RELEVANCY("relevancy"),
    POPULARITY("popularity"),
    PUBLISHED_AT("publishedAt");

    companion object {
        fun getSortTypeFromPosition(position: Int): SortType {
            return when (position) {
                1 -> POPULARITY
                2 -> PUBLISHED_AT
                else -> RELEVANCY
            }
        }
    }
}
