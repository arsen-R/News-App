package com.example.newsapp.api;

import com.example.newsapp.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleNewsApi {
    private static ArticleNewsApi articleNewsApi;
    private Retrofit retrofit;

    public ArticleNewsApi() {
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
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
