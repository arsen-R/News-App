package com.example.newsapp.api

import com.example.newsapp.model.NewsResult
import retrofit2.http.GET
import com.example.newsapp.utils.Constants
import retrofit2.Response
import retrofit2.http.Query

interface ArticleNewsService {
    @GET("news")
    suspend fun getAllArticleNews(
        @Query("category") category: String = "top",
        @Query("country") country: String = "us",
        @Query("page") pageNumber: Int = 0,
        @Query("apikey") apikey: String = Constants.API_KEY
    ): Response<NewsResult>
}