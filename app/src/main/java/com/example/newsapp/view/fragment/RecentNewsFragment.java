package com.example.newsapp.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newsapp.R;
import com.example.newsapp.adapter.ArticleNewsAdapter;
import com.example.newsapp.api.ArticleNewsApi;
import com.example.newsapp.api.ArticleNewsService;
import com.example.newsapp.model.ArticleNews;
import com.example.newsapp.model.ResultNews;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecentNewsFragment extends Fragment {
    private RecyclerView recentNewsRecyclerView;

    private ArticleNewsAdapter articleNewsAdapter;
    private ArticleNewsService articleNewsService;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recents_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        articleNewsService = ArticleNewsApi.getInstance().getArticleNewsService();
        recentNewsRecyclerView = view.findViewById(R.id.recentNewsRecyclerView);
        getAllNews();
    }

    private void getAllNews() {
        Call<ResultNews> call = articleNewsService.getAllArticleNews("pub_86977c4beb3733e38eee74f8b77a4c2c932a",
                "top", "uk");
        call.enqueue(new Callback<ResultNews>() {
            @Override
            public void onResponse(Call<ResultNews> call, Response<ResultNews> response) {
                ResultNews resultNews = response.body();
                articleNewsAdapter = new ArticleNewsAdapter(resultNews);
                recentNewsRecyclerView.setHasFixedSize(true);
                recentNewsRecyclerView.setAdapter(articleNewsAdapter);
                articleNewsAdapter.setOnItemClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                        int position = viewHolder.getAdapterPosition();
                        ArticleNews articleNews = resultNews.getArticleNews().get(position);

                        NavDirections action =
                                RecentNewsFragmentDirections.actionRecentNewsFragmentToArticleNewsFragment(articleNews.getLink());
                        Navigation.findNavController(view).navigate(action);
                    }
                });
                Log.d("ArticleNews", resultNews.toString());
            }

            @Override
            public void onFailure(Call<ResultNews> call, Throwable t) {
                Log.e("ArticleNews", t.getMessage());
            }
        });
    }
}