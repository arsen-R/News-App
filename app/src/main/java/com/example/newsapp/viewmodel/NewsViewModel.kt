package com.example.newsapp.viewmodel

import android.util.Log
import com.example.newsapp.repository.NewsRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.ArticleNews
import com.example.newsapp.model.NewsResult
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {
    val newsMutableLiveData = MutableLiveData<Resource<NewsResult>>()
    private var articleNewsPage: Int = 0
    private var newsResult: NewsResult? = null

    val searchNewsMutableLiveData = MutableLiveData<Resource<NewsResult>>()
    private var searchNewsPage: Int = 0
    private var searchArticleNews: NewsResult? = null
    private var oldSearchQuery: String? = null
    private var newSearchQuery: String? = null

    fun getArticleNews(category: String, country: String) = viewModelScope.launch {
        loadArticleNews(category, country)
    }

    fun getSearchNews(query: String, country: String) = viewModelScope.launch {
        loadSearchArticle(query, country)
    }
    private fun handleArticleNews(response: Response<NewsResult>): Resource<NewsResult> {
        if (response.isSuccessful) {
            response?.body()?.let { result ->
                articleNewsPage++
                if (newsResult == null) {
                    newsResult = result
                } else {
                    var oldArticle = newsResult?.articleNews
                    var newArticle = result.articleNews
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(newsResult ?: result)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchArticle(response: Response<NewsResult>) : Resource<NewsResult> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                if (searchArticleNews == null || newSearchQuery != oldSearchQuery) {
                    oldSearchQuery = newSearchQuery
                    searchNewsPage = 0
                    searchArticleNews = result
                } else {
                    searchNewsPage++
                    var oldArticle = searchArticleNews?.articleNews
                    var newArticle = result.articleNews
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(searchArticleNews ?: result)
            }
        }
        return Resource.Error(response.message())
    }
    private suspend fun loadArticleNews(category: String, country: String) {
        newsMutableLiveData.postValue(Resource.Loading())
        try {
            val response = repository.getArticleNews(category, country, articleNewsPage)
            newsMutableLiveData.postValue(handleArticleNews(response))
            Log.d("LogArticleNews", response.body()?.nextPage.toString())
        } catch (t: Throwable) {
            newsMutableLiveData.postValue(Resource.Error("Network error"))
            Log.e("LogArticleNewsError", t.message.toString())
        }
    }
    private suspend fun loadSearchArticle(query: String, country: String) {
        newSearchQuery = query
        searchNewsMutableLiveData.postValue(Resource.Loading())
        try {
            val response = repository.searchArticle(query, country, searchNewsPage)
            searchNewsMutableLiveData.postValue(handleSearchArticle(response))
            Log.d("LogArticleNews", response.body()?.nextPage.toString())
        } catch (t: Throwable) {
            newsMutableLiveData.postValue(Resource.Error("Network error"))
            Log.e("LogArticleNewsError", t.message.toString())
        }
    }

    fun saveArticle(articleNews: ArticleNews) = repository.addArticleNews(articleNews)

    fun getSavedArticles() = repository.getAllSavedArticleNews()

    fun deleteArticleNews(articleNews: ArticleNews) = repository.deleteArticleNews(articleNews)

    override fun onCleared() {
        super.onCleared()
    }
}