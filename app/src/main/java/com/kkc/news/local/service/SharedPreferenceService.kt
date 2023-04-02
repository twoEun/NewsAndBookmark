package com.kkc.news.local.service

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class SharedPreferenceServiceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val editor: Editor
) : SharedPreferenceService {
    private val LATEST_SEARCH_KEYWORD = "LATEST_SEARCH_KEYWORD"

    override fun getLatestSearchKeyword(): String {
        return sharedPreferences.getString(LATEST_SEARCH_KEYWORD, "") ?: ""
    }

    override fun setLatestSearchKeyword(keyword: String): Completable {
        editor.putString(LATEST_SEARCH_KEYWORD, keyword).apply()
        return Completable.complete()
    }
}

interface SharedPreferenceService {
    fun getLatestSearchKeyword(): String
    fun setLatestSearchKeyword(keyword: String): Completable
}
