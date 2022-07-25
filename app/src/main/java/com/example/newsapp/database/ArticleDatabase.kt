package com.example.newsapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.model.ArticleNews

@Database(entities = [ArticleNews::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var database: ArticleDatabase? = null
        private const val DATABASE_NAME = "news.db"

        fun getInstance(context: Context): ArticleDatabase {
            return database ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    ArticleDatabase::class.java,
                    DATABASE_NAME
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                database = instance
                instance
            }
        }
    }

    abstract fun articleDao(): ArticleDao
}