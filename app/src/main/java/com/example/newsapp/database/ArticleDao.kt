package com.example.newsapp.database

import android.icu.text.CaseMap
import androidx.room.*
import com.example.newsapp.model.ArticleNews
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM news_article")
    fun getAllSavedArticle(): Flow<List<ArticleNews>>

    @Query("SELECT EXISTS (SELECT 1 FROM news_article WHERE title = :title)")
    fun getSavedArticleByTitle(title: String) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveArticle(articleNews: ArticleNews): Long

    @Query("DELETE FROM news_article WHERE title = :title")
    fun deleteArticle(title: String)
}