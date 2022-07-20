package com.example.newsapp.repository

import com.example.newsapp.network.ArticleNewsService
import com.example.newsapp.data.ArticleDatabase
import com.example.newsapp.model.ArticleNews

class NewsRepository(private val articleNewsService: ArticleNewsService, private val database: ArticleDatabase) {

    suspend fun getArticleNews(category: String, country: String, pageNumber: Int) =
        articleNewsService.getAllArticleNews(category, country, pageNumber)

    suspend fun searchArticle(query: String, country: String, pageNumber: Int) =
        articleNewsService.searchArticleNews(query, country, pageNumber)

    fun getAllSavedArticleNews() = database.articleDao().getAllArticleNews()

    fun addArticleNews(articleNews: ArticleNews) = database.articleDao().addArticleNews(articleNews)

    fun deleteArticleNews(articleNews: ArticleNews) = database.articleDao().deleteArticleNews(articleNews)
}