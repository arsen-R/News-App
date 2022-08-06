package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.adapter.ArticleNewsAdapter.ArticleNewsViewHolder
import com.example.newsapp.databinding.NewsListItemBinding
import com.example.newsapp.model.ArticleNews
import com.example.newsapp.utils.DiffCallBack

class ArticleNewsAdapter : PagingDataAdapter<ArticleNews, ArticleNewsViewHolder>(DiffCallBack) {
    private var onClickListener: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleNewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NewsListItemBinding.inflate(inflater, parent, false)
        return ArticleNewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleNewsViewHolder, position: Int) {
        val articleNews = getItem(position) ?: return
        holder.bind(articleNews)
    }

    fun setOnItemClickListener(onItemClickListener: View.OnClickListener?) {
        onClickListener = onItemClickListener
    }

    inner class ArticleNewsViewHolder(val binding: NewsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(articleNews: ArticleNews?) {
            with(binding) {
                textTitleNews.text = articleNews?.title

                textSourceName.text =
                    articleNews?.sourceId?.substring(0, 1)
                        ?.uppercase() + articleNews?.sourceId?.substring(1)
                        ?.lowercase()

                textPublishedNews.text = articleNews?.pubDate

                if (articleNews?.imageUrl?.isNotEmpty() == true) {
                    Glide.with(imageArticleNews.context)
                        .load(articleNews.imageUrl)
                        .fitCenter()
                        .into(imageArticleNews)
                } else {
                    Glide.with(imageArticleNews.context).clear(imageArticleNews)
                }
            }
        }

        init {
            binding.root.tag = this
            binding.root.setOnClickListener(onClickListener)
        }
    }

}