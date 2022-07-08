package com.example.newsapp.data

import android.content.Context
import androidx.room.*
import com.example.newsapp.model.ArticleNews
import com.example.newsapp.utils.Constants

@Database(entities = [ArticleNews::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {
    companion object {
        private var database: ArticleDatabase? = null
        private const val NAME_DATABASE = "article_news.db"
        private val LOCK = Any()

        fun getInstance(context: Context): ArticleDatabase {
            synchronized(LOCK) {
                database?.let { return it }
                val instance = Room.databaseBuilder(
                    context,
                    ArticleDatabase::class.java,
                    NAME_DATABASE
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                database = instance
                return instance
            }
        }
    }

    abstract fun articleDao(): ArticleDao
}