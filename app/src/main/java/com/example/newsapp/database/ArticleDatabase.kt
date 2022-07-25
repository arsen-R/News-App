package com.example.newsapp.database

import android.content.Context
import androidx.room.*
import com.example.newsapp.model.ArticleNews

@Database(entities = [ArticleNews::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {
    companion object {
        private var database: ArticleDatabase? = null
        private const val NAME_DATABASE = "articles_news.db"
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