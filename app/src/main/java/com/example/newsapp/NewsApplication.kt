package com.example.newsapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.newsapp.database.ArticleDatabase
import com.example.newsapp.network.ArticleNewsApi
import com.example.newsapp.network.ArticleNewsService
import com.example.newsapp.repository.NewsRepositoryImpl
import com.example.newsapp.utils.ThemeProvider

class NewsApplication : Application() {
    private val articleNewsService: ArticleNewsService by lazy { ArticleNewsApi.articleNewsService }
    private val articleDatabase: ArticleDatabase by lazy { ArticleDatabase.getInstance(this) }
    val repository: NewsRepositoryImpl by lazy { NewsRepositoryImpl(articleNewsService, articleDatabase) }

    override fun onCreate() {
        super.onCreate()
        val theme = ThemeProvider(this).getThemeFromPreferences()
        AppCompatDelegate.setDefaultNightMode(theme)
    }
}