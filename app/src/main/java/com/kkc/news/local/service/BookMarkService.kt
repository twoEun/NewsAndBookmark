package com.kkc.news.local.service

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kkc.news.local.roomData.BookMark
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface BookMarkService {
    @Query("SELECT * FROM BookMark LIMIT :pageSize OFFSET :page * :pageSize")
    fun getPagingBookmarks(page: Int, pageSize: Int): Single<List<BookMark>>

    @Query("SELECT * FROM BookMark")
    fun getAllBookMarks(): Observable<List<BookMark>>

    @Insert
    fun addBookMark(bookMark: BookMark): Completable

    @Query("DELETE FROM Bookmark WHERE `index` = :index")
    fun deleteBookMark(index: Int): Completable

    @Delete
    fun deleteBookMark(bookMark: BookMark): Completable

    @Query("SELECT `index` FROM BookMark ORDER BY `index` DESC LIMIT 1")
    fun getLastIndex(): Single<Int>
}
