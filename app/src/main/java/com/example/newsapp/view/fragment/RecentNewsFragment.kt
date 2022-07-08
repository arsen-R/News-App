package com.example.newsapp.view.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.NewsApplication
import com.example.newsapp.R
import com.example.newsapp.adapter.ArticleNewsAdapter
import com.example.newsapp.api.ArticleNewsApi
import com.example.newsapp.model.NewsResult
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.view.MainActivity
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFactory

class RecentNewsFragment : Fragment(R.layout.fragment_recents_news) {
    private val articleNewsAdapter: ArticleNewsAdapter by lazy { ArticleNewsAdapter() }

    private val newsViewModel: NewsViewModel by viewModels {
        NewsViewModelFactory((activity?.application as NewsApplication).repository)
    }

    private val onItemClickListener = View.OnClickListener { view: View ->
        val viewHolder = view.tag as RecyclerView.ViewHolder
        val position = viewHolder.adapterPosition

        val articleNews = articleNewsAdapter.articleNewsList[position]

        val action =
            RecentNewsFragmentDirections.actionRecentNewsFragmentToArticleNewsFragment(articleNews)

        findNavController(view).navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val recentNewsRecyclerView: RecyclerView = view.findViewById(R.id.recentNewsRecyclerView)
        recentNewsRecyclerView.adapter = articleNewsAdapter
        recentNewsRecyclerView.setHasFixedSize(true)
        articleNewsAdapter.setOnItemClickListener(onItemClickListener)

        newsViewModel.getNewsMutableLiveData("top", "ua")
            .observe(viewLifecycleOwner) { resultNews: NewsResult ->
                articleNewsAdapter.articleNewsList = resultNews.articleNews
            }
    }
}