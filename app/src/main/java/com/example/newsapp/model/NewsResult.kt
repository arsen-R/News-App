package com.example.newsapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NewsResult(
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("totalResults")
    @Expose
    val totalResults: Int,
    @SerializedName("results")
    @Expose
    val articleNews: List<ArticleNews>,
    @SerializedName("nextPage")
    @Expose
    val nextPage: Int
)