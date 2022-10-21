package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.newsapp.databinding.NewsListItemBinding
import com.example.newsapp.model.ArticleNews
import com.example.newsapp.utils.DiffCallBack

class SavedArticleAdapter : ListAdapter<ArticleNews, SavedArticleAdapter.SavedArticleViewHolder>(DiffCallBack){
    private var onClickListener: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NewsListItemBinding.inflate(inflater, parent, false)
        return SavedArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedArticleViewHolder, position: Int) {
        val articleNews = getItem(position)

        with(holder.binding) {
            textTitleNews.text = articleNews.title

            textSourceName.text =
                articleNews.sourceId?.substring(0, 1)
                    ?.uppercase() + articleNews.sourceId?.substring(1)
                    ?.lowercase()

            textPublishedNews.text = articleNews.getFormattedTime()

            if (articleNews.imageUrl?.isNotBlank() == true) {
                Glide.with(imageArticleNews.context)
                    .load(articleNews.imageUrl)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(imageArticleNews)
            } else {
                Glide.with(imageArticleNews.context).clear(imageArticleNews)
            }
        }
    }

    fun setOnItemClickListener(onItemClickListener: View.OnClickListener?) {
        onClickListener = onItemClickListener
    }

    inner class SavedArticleViewHolder(val binding: NewsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.tag = this
            binding.root.setOnClickListener(onClickListener)
        }
    }

    //override fun getItemCount(): Int = articleListDiffer.currentList.size
}