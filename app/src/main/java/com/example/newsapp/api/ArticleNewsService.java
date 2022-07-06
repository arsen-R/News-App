package com.example.newsapp.api;

import com.example.newsapp.model.ResultNews;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ArticleNewsService {
    @GET("news")
    Single<ResultNews> getAllArticleNews(
            @Query("apikey") String apikey,
            @Query("category") String category,
            @Query("country") String country);
}
