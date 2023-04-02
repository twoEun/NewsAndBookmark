package com.kkc.news.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kkc.news.R

@BindingAdapter("newsContent")
fun TextView.setNewsContent(content: String?) {
    content?.let {
        this.text = this.context.getString(R.string.news_content, it)
    } ?: run {
        this.text = ""
    }
}

@BindingAdapter("urlImages")
fun ImageView.bindImageScale(imageUrl: String?) {
    imageUrl?.let {
        Glide.with(this.context)
            .load(it)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .fitCenter()
            .into(this)
    }
}
