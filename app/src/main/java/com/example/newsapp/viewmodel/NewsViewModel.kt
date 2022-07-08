package com.example.newsapp.viewmodel

import android.util.Log
import com.example.newsapp.repository.NewsRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.model.ArticleNews
import com.example.newsapp.model.NewsResult
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val newsMutableLiveData = MutableLiveData<NewsResult>()
    private val compositeDisposable = CompositeDisposable()

    fun getNewsMutableLiveData(category: String, country: String): MutableLiveData<NewsResult> {
        loadArticleNews(category, country)
        return newsMutableLiveData
    }

    private fun loadArticleNews(category: String, country: String) {
        val disposable = repository.getArticleNews(category, country)
            .delaySubscription(10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe (
                {
                    Log.d("LogArticleNews", it.toString())
                    newsMutableLiveData.postValue(it)
                },{
                    Log.e("LogArticleNewsError", it.message.toString())
                }
            )
        compositeDisposable.add(disposable)
    }

    fun saveArticle(articleNews: ArticleNews) = repository.addArticleNews(articleNews)

    fun getSavedArticles() = repository.getAllSavedArticleNews()

    fun deleteArticleNews(articleNews: ArticleNews) = repository.deleteArticleNews(articleNews)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}