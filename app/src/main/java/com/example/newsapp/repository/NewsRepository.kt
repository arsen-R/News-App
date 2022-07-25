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

    fun getAllSavedArticleNews() = database.articleDao().getAllArticleNews()

    fun addArticleNews(articleNews: ArticleNews) = database.articleDao().addArticleNews(articleNews)

    fun deleteArticleNews(articleNews: ArticleNews) =
        database.articleDao().deleteArticleNews(articleNews)
}