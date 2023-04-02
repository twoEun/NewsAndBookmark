package com.kkc.news.local.repository

import com.kkc.news.local.service.SharedPreferenceService
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class SharedPreferenceRepositoryImpl @Inject constructor(
    private val sharedPreferenceService: SharedPreferenceService
) : SharedPreferenceRepository {
    override fun getLatestSearchKeyword(): String {
        return sharedPreferenceService.getLatestSearchKeyword()
    }

    override fun setLatestSearchKeyword(keyword: String): Completable {
        return sharedPreferenceService.setLatestSearchKeyword(keyword)
    }
}

interface SharedPreferenceRepository {
    fun getLatestSearchKeyword(): String
    fun setLatestSearchKeyword(keyword: String): Completable
}
