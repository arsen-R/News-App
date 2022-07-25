package com.example.newsapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapp.model.ArticleNews

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article_news")
    fun getAllArticleNews(): LiveData<List<ArticleNews>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addArticleNews(articleNews: ArticleNews): Long

    @Delete
    fun deleteArticleNews(articleNews: ArticleNews)
}