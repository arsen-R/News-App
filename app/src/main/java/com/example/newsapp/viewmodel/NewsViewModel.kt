package com.example.newsapp.viewmodel

import android.util.Log
import com.example.newsapp.repository.NewsRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.newsapp.model.ArticleNews
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {
    private val categoryFlow: MutableStateFlow<String?> = MutableStateFlow("top")
    private val countryFlow: MutableStateFlow<String?> = MutableStateFlow("")
    private val searchQueryFlow: MutableStateFlow<String?> = MutableStateFlow("")

    val articleNews = combine(categoryFlow, countryFlow) { (category, country) ->
        PreferenceData(category, country)
    }.flatMapLatest { repository.getNews(it.category.toString(), it.country.toString()) }
        .cachedIn(viewModelScope)

    val searchNews = combine(searchQueryFlow, countryFlow) { (query, country) ->
        SearchQueryData(query, country)
    }.flatMapLatest { repository.searchArticle(it.query.toString(), it.country.toString()) }
        .cachedIn(viewModelScope)

    fun setArticleNews(category: String?) {
        categoryFlow.tryEmit(category)
    }

    fun setCountry(country: String?) {
        countryFlow.value = country
    }

    fun setSearchQuery(query: String?) {
        searchQueryFlow.tryEmit(query)
    }

    val getAllSavedArticleFlow = repository.getSavedArticleNews

    fun saveArticle(articleNews: ArticleNews) =
        repository.saveArticle(articleNews)

    fun deleteArticle(title: String) = repository.deleteArticle(title)

    fun savedArticleByTitle(title: String) = repository.getSavedArticleNewsByTitle(title)

    data class PreferenceData(
        val category: String?,
        val country: String?
    )

    data class SearchQueryData(
        val query: String?,
        val country: String?
    )

    override fun onCleared() {
        super.onCleared()
    }
}
