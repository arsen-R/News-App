package com.example.newsapp.api

import com.example.newsapp.model.NewsResult
import retrofit2.http.GET
import com.example.newsapp.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Query

interface ArticleNewsService {
    @GET("news")
    suspend fun getAllArticleNews(
        @Query("apikey") apikey: String = "pub_86977c4beb3733e38eee74f8b77a4c2c932a",
        @Query("category") category: String = "top",
        @Query("country") country: String = "us",
        @Query("page") pageNumber: Int = 0
    ): Response<NewsResult>
}