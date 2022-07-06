package com.example.newsapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.newsapp.model.ResultNews;
import com.example.newsapp.repository.NewsRepository;
import com.example.newsapp.utils.Constants;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NewsViewModel extends ViewModel {
    private NewsRepository repository;
    private MutableLiveData<ResultNews> newsMutableLiveData = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public NewsViewModel(NewsRepository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ResultNews> getNewsMutableLiveData(String category, String country) {
        loadArticleNews(category, country);
        return newsMutableLiveData;
    }
    private void loadArticleNews(String category, String country) {
        Disposable disposable = repository.getArticleNews(Constants.API_KEY, category, country)
                .delaySubscription(10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<ResultNews>() {
                    @Override
                    public void onSuccess(@NonNull ResultNews resultNews) {
                        Log.d("LogArticleNews", resultNews.toString());
                        newsMutableLiveData.postValue(resultNews);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("LogArticleNews", e.getMessage());
                    }
                });
        compositeDisposable.add(disposable);
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
