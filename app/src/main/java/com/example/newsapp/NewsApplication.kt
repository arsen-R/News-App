package com.example.newsapp

import android.app.Application
import com.example.newsapp.api.ArticleNewsApi
import com.example.newsapp.data.ArticleDatabase
import com.example.newsapp.repository.NewsRepository

class NewsApplication : Application() {
    val repository: NewsRepository by lazy { NewsRepository(ArticleNewsApi.articleNewsService, ArticleDatabase.getInstance(this)) }
}