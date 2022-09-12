package com.example.newsapp.repository

import androidx.paging.PagingData
import com.example.newsapp.model.ArticleNews
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(category: String, country: String): Flow<PagingData<ArticleNews>>

    fun searchArticle(query: String, country: String): Flow<PagingData<ArticleNews>>

    val getSavedArticleNews: Flow<List<ArticleNews>>

    fun getSavedArticleNewsByTitle(title: String) : Int

    fun saveArticle(articleNews: ArticleNews)

    fun deleteArticle(title: String)
}