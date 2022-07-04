package com.example.newsapp.api;

import com.example.newsapp.model.ResultNews;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
// https://newsdata.io/api/1/news?apikey=pub_86977c4beb3733e38eee74f8b77a4c2c932a&category=business&language=uk&country=ua
public interface ArticleNewsService {
    @GET("news")
    Call<ResultNews> getAllArticleNews(
            @Query("apikey") String apikey,
            @Query("category") String category,
            @Query("language") String language);
}
