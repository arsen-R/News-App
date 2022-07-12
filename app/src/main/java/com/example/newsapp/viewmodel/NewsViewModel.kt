package com.example.newsapp.viewmodel

import android.util.Log
import com.example.newsapp.repository.NewsRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.model.ArticleNews
import com.example.newsapp.model.NewsResult
import com.example.newsapp.utils.Resource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val newsMutableLiveData = MutableLiveData<Resource<NewsResult>>()
    private val compositeDisposable = CompositeDisposable()
    var articleNewsPage: Int = 0
    private var newsResult: NewsResult? = null

    fun getNewsMutableLiveData(category: String, country: String): MutableLiveData<Resource<NewsResult>> {
        loadArticleNews(category, country)
        return newsMutableLiveData
    }

    private fun loadArticleNews(category: String, country: String) {
        newsMutableLiveData.postValue(Resource.Loading())
        val disposable = repository.getArticleNews(category, country, articleNewsPage)
            .delaySubscription(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                {
                    articleNewsPage++
                    if (newsResult == null) {
                        newsResult = it
                    } else {
                        var oldArticle = newsResult?.articleNews
                        var newArticle = it.articleNews
                        oldArticle?.addAll(newArticle)
                    }
                    newsMutableLiveData.postValue(Resource.Success(newsResult ?: it))
                    Log.d("LogArticleNews", it.toString())
                },{
                    newsMutableLiveData.postValue(Resource.Error("Network error"))
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