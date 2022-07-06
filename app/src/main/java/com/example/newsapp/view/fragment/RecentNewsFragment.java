package com.example.newsapp.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.R;
import com.example.newsapp.adapter.ArticleNewsAdapter;
import com.example.newsapp.api.ArticleNewsApi;
import com.example.newsapp.model.ArticleNews;
import com.example.newsapp.repository.NewsRepository;
import com.example.newsapp.viewmodel.NewsViewModel;
import com.example.newsapp.viewmodel.NewsViewModelFactory;

public class RecentNewsFragment extends Fragment {
    private RecyclerView recentNewsRecyclerView;

    private ArticleNewsAdapter articleNewsAdapter;
    private NewsRepository newsRepository;
    private NewsViewModel newsViewModel;
    private NewsViewModelFactory newsViewModelFactory;

    private View.OnClickListener onItemClickListener = (click) -> {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) click.getTag();
            int position = viewHolder.getAdapterPosition();

            ArticleNews articleNews = articleNewsAdapter.getArticleNewsList().get(position);
            NavDirections action = RecentNewsFragmentDirections.actionRecentNewsFragmentToArticleNewsFragment(articleNews.getLink());
            Navigation.findNavController(click).navigate(action);
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recents_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newsRepository = new NewsRepository(ArticleNewsApi.getInstance().getArticleNewsService());
        newsViewModelFactory = new NewsViewModelFactory(newsRepository);
        newsViewModel = new ViewModelProvider(this, newsViewModelFactory).get(NewsViewModel.class);
        articleNewsAdapter = new ArticleNewsAdapter(getContext());

        recentNewsRecyclerView = view.findViewById(R.id.recentNewsRecyclerView);
        recentNewsRecyclerView.setAdapter(articleNewsAdapter);
        articleNewsAdapter.setOnItemClickListener(onItemClickListener);

        newsViewModel.getNewsMutableLiveData("top", "ua").observe(getViewLifecycleOwner(), resultNews -> {
            articleNewsAdapter.setArticleNewsList(resultNews.getArticleNews());
        });
    }
}