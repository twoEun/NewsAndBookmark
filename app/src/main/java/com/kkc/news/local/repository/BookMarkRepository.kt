package com.kkc.news.local.repository

import com.kkc.news.local.roomData.BookMark
import com.kkc.news.local.service.BookMarkService
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class BookMarkRepositoryImpl @Inject constructor(
    private val bookMarkService: BookMarkService
) : BookMarkRepository {
    override fun getAllBookMarks(): Observable<List<BookMark>> {
        return bookMarkService.getAllBookMarks()
    }

    override fun getBookMarkList(page: Int, pageSize: Int): Single<List<BookMark>> {
        return bookMarkService.getPagingBookmarks(page, pageSize)
    }

    override fun addBookMark(bookMark: BookMark): Completable {
        return bookMarkService.addBookMark(bookMark)
    }

    override fun deleteBookMark(bookmarkIdx: Int): Completable {
        return bookMarkService.deleteBookMark(bookmarkIdx)
    }

    override fun deleteBookMark(bookMark: BookMark): Completable {
        return bookMarkService.deleteBookMark(bookMark)
    }

    override fun getLastIndex(): Single<Int> {
        return bookMarkService.getLastIndex()
    }
}

interface BookMarkRepository {
    fun getAllBookMarks(): Observable<List<BookMark>>
    fun getBookMarkList(page: Int, pageSize: Int): Single<List<BookMark>>
    fun addBookMark(bookMark: BookMark): Completable
    fun deleteBookMark(bookmarkIdx: Int): Completable
    fun deleteBookMark(bookMark: BookMark): Completable
    fun getLastIndex(): Single<Int>
}
