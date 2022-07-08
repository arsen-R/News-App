package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.adapter.ArticleNewsAdapter.ArticleNewsViewHolder
import com.example.newsapp.model.ArticleNews

class ArticleNewsAdapter() : RecyclerView.Adapter<ArticleNewsViewHolder>() {
    var articleNewsList: List<ArticleNews> = listOf()
        set (value) {
            field = value
            notifyDataSetChanged()
        }

    private var onClickListener: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleNewsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.news_list_item, parent, false)
        return ArticleNewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleNewsViewHolder, position: Int) {
        val articleNews = articleNewsList[position]
        with(holder) {
            textTitleNews.text = articleNews.title
            textSourceNews.text =
                articleNews.sourceId.substring(0, 1).uppercase() + articleNews.sourceId.substring(1)
                    .lowercase()
            textPublishedNews.text = articleNews.pubDate
            Glide.with(imageArticleNews.context)
                .load(articleNews.imageUrl)
                .into(imageArticleNews)
        }
    }

    override fun getItemCount(): Int {
        return articleNewsList.size
    }

    fun setOnItemClickListener(onItemClickListener: View.OnClickListener?) {
        onClickListener = onItemClickListener
    }

    inner class ArticleNewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageArticleNews: ImageView
        var textTitleNews: TextView
        var textSourceNews: TextView
        var textPublishedNews: TextView

        init {
            imageArticleNews = view.findViewById(R.id.imageArticleNews)
            textTitleNews = view.findViewById(R.id.textTitleNews)
            textSourceNews = view.findViewById(R.id.textSourceName)
            textPublishedNews = view.findViewById(R.id.textPublishedNews)
            view.tag = this
            view.setOnClickListener(onClickListener)
        }
    }
}