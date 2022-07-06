package com.example.newsapp.repository;

import com.example.newsapp.api.ArticleNewsService;
import com.example.newsapp.model.ResultNews;

import io.reactivex.rxjava3.core.Single;

public class NewsRepository {
    private ArticleNewsService articleNewsService;

    public NewsRepository(ArticleNewsService articleNewsService) {
        this.articleNewsService = articleNewsService;
    }
    public Single<ResultNews> getArticleNews(String apiKey, String category, String country) {
        return articleNewsService.getAllArticleNews(apiKey, category, country);
    }
}
