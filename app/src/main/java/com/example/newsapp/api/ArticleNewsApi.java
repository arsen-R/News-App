package com.example.newsapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleNewsApi {
    private static ArticleNewsApi articleNewsApi;
    private Retrofit retrofit;

    public ArticleNewsApi() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://newsdata.io/api/1/")
                .build();
    }
    public static ArticleNewsApi getInstance() {
        if (articleNewsApi == null) {
            articleNewsApi = new ArticleNewsApi();
        }
        return articleNewsApi;
    }
    public ArticleNewsService getArticleNewsService() {
        return retrofit.create(ArticleNewsService.class);
    }
}
