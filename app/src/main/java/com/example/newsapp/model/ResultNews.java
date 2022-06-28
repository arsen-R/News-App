package com.example.newsapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultNews {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("totalResults")
    @Expose
    private int totalResults;
    @SerializedName("results")
    @Expose
    private List<ArticleNews> articleNews;

    public ResultNews(String status, int totalResults, List<ArticleNews> articleNews) {
        this.status = status;
        this.totalResults = totalResults;
        this.articleNews = articleNews;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<ArticleNews> getArticleNews() {
        return articleNews;
    }

    public void setArticleNews(List<ArticleNews> articleNews) {
        this.articleNews = articleNews;
    }
}
