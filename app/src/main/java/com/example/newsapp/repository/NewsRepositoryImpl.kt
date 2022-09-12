package com.example.newsapp.repository

import androidx.paging.*
import com.example.newsapp.database.ArticleDatabase
import com.example.newsapp.model.ArticleNews
import com.example.newsapp.network.ArticleNewsService
import com.example.newsapp.paging.NewsPagingSource
import com.example.newsapp.paging.SearchNewsPagingSource
import kotlinx.coroutines.flow.Flow

class NewsRepositoryImpl(
    private val articleNewsService: ArticleNewsService,
    private val database: ArticleDatabase
) : NewsRepository {
    override fun getNews(category: String, country: String): Flow<PagingData<ArticleNews>> {
        return Pager(
            PagingConfig(
                pageSize = 2,
                enablePlaceholders = true
            )
        ) {
            NewsPagingSource(articleNewsService, category, country)
        }.flow
    }

    override fun searchArticle(query: String, country: String): Flow<PagingData<ArticleNews>> {
        return Pager(
            PagingConfig(
                pageSize = 2,
                enablePlaceholders = true
            )
        ) {
            SearchNewsPagingSource(articleNewsService, query, country)
        }.flow
    }

    override val getSavedArticleNews: Flow<List<ArticleNews>> = database.articleDao().getAllSavedArticle()

    override fun getSavedArticleNewsByTitle(title: String) = database.articleDao().getSavedArticleByTitle(title)

    override fun saveArticle(articleNews: ArticleNews) {
        database.articleDao().saveArticle(articleNews)
    }

    override fun deleteArticle(title: String) {
        database.articleDao().deleteArticle(title)
    }
}