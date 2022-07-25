package com.example.newsapp.repository

import androidx.paging.*
import com.example.newsapp.database.ArticleDatabase
import com.example.newsapp.model.ArticleNews
import com.example.newsapp.network.ArticleNewsService
import com.example.newsapp.paging.NewsPagingSource
import com.example.newsapp.paging.SearchNewsPagingSource
import kotlinx.coroutines.flow.Flow

class NewsRepository(
    private val articleNewsService: ArticleNewsService,
    private val database: ArticleDatabase
) {
    fun getNews(category: String, country: String): Flow<PagingData<ArticleNews>> {
        return Pager(
            PagingConfig(
                pageSize = 2,
                enablePlaceholders = true
            )
        ) {
            NewsPagingSource(articleNewsService, category, country)
        }.flow
    }

    fun searchArticle(query: String, country: String): Flow<PagingData<ArticleNews>> {
        return Pager(
            PagingConfig(
                pageSize = 2,
                enablePlaceholders = true
            )
        ) {
            SearchNewsPagingSource(articleNewsService, query, country)
        }.flow
    }

    val getSavedArticleNews: Flow<List<ArticleNews>> = database.articleDao().getAllSavedArticle()

    fun getSavedArticleNewsByTitle(title: String) = database.articleDao().getSavedArticleByTitle(title)

    fun saveArticle(articleNews: ArticleNews) {
        database.articleDao().saveArticle(articleNews)
    }

    fun deleteArticle(title: String) {
        database.articleDao().deleteArticle(title)
    }
}