package com.example.newsapp.repository

import com.example.newsapp.api.ArticleNewsService
import com.example.newsapp.data.ArticleDatabase
import com.example.newsapp.model.ArticleNews
import com.example.newsapp.utils.Constants

class NewsRepository(private val articleNewsService: ArticleNewsService, private val database: ArticleDatabase) {
    fun getArticleNews(category: String, country: String) =
        articleNewsService.getAllArticleNews(Constants.API_KEY,category, country)


    fun getAllSavedArticleNews() = database.articleDao().getAllArticleNews()

    fun addArticleNews(articleNews: ArticleNews) = database.articleDao().addArticleNews(articleNews)

    fun deleteArticleNews(articleNews: ArticleNews) = database.articleDao().deleteArticleNews(articleNews)
}