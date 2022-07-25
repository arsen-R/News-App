package com.example.newsapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.newsapp.model.ArticleNews

object DiffCallBack : DiffUtil.ItemCallback<ArticleNews>() {
    override fun areItemsTheSame(oldItem: ArticleNews, newItem: ArticleNews): Boolean {
        return oldItem.link == newItem.link
    }

    override fun areContentsTheSame(oldItem: ArticleNews, newItem: ArticleNews): Boolean {
        return oldItem == newItem
    }
}