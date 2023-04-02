package com.kkc.news.entity.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ArticleSourceData(
    @SerializedName("id")
    val sourceId: String?,

    @SerializedName("name")
    val sourceName: String
) : Parcelable
