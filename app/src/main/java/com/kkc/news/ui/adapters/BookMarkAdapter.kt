package com.kkc.news.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kkc.news.R
import com.kkc.news.databinding.ItemBookmarkBinding
import com.kkc.news.local.roomData.BookMark
import com.kkc.news.ui.interfaces.BookMarkItemClick

class BookMarkAdapter(private val bookMarkItemClick: BookMarkItemClick) : ListAdapter<BookMark, BookMarkHolder>(bookmarkDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkHolder {
        val binding = DataBindingUtil.inflate<ItemBookmarkBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_bookmark,
            parent,
            false
        )

        binding.root.setOnClickListener {
            binding.bookmark?.let {
                bookMarkItemClick.onBookMarkItemClick(it)
            }
        }

        return BookMarkHolder(binding)
    }

    override fun onBindViewHolder(holder: BookMarkHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}

val bookmarkDiff = object : DiffUtil.ItemCallback<BookMark>() {
    override fun areItemsTheSame(oldItem: BookMark, newItem: BookMark): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: BookMark, newItem: BookMark): Boolean {
        return oldItem.index == newItem.index
    }

}

class BookMarkHolder(private val binding: ItemBookmarkBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(item: BookMark?) {
        item?.let {
            binding.bookmark = it
        }
    }
}
