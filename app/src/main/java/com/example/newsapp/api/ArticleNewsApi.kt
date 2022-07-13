package com.example.newsapp.api

import retrofit2.Retrofit
import com.example.newsapp.api.ArticleNewsService
import com.example.newsapp.api.ArticleNewsApi
import com.example.newsapp.utils.Constants
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ArticleNewsApi {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.BASE_URL)
        .build()

    val articleNewsService: ArticleNewsService = retrofit.create(ArticleNewsService::class.java)
}