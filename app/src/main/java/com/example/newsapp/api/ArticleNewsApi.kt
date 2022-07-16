package com.example.newsapp.api

import com.example.newsapp.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ArticleNewsApi {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.BASE_URL)
        .build()

    val articleNewsService: ArticleNewsService = retrofit.create(ArticleNewsService::class.java)
}