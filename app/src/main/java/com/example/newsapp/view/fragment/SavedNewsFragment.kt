package com.example.newsapp.view.fragment

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.NewsApplication
import com.example.newsapp.R
import com.example.newsapp.adapter.ArticleNewsAdapter
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFactory

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
    private val articleNewsAdapter: ArticleNewsAdapter by lazy { ArticleNewsAdapter() }

    private val newsViewModel: NewsViewModel by viewModels {
        NewsViewModelFactory((activity?.application as NewsApplication).repository)
    }

    private val onItemClickListener = View.OnClickListener { view: View ->
        val viewHolder = view.tag as RecyclerView.ViewHolder
        val position = viewHolder.adapterPosition

        val articleNews = articleNewsAdapter.articleNewsList[position]

        val action =
            SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleNewsFragment(articleNews)

        Navigation.findNavController(view).navigate(action)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val savedNewsRecyclerView: RecyclerView = view.findViewById(R.id.savedNewsRecyclerView)
        savedNewsRecyclerView.setHasFixedSize(true)
        savedNewsRecyclerView.adapter = articleNewsAdapter
        articleNewsAdapter.setOnItemClickListener(onItemClickListener)

        newsViewModel.getSavedArticles().observe(viewLifecycleOwner) { article ->
            articleNewsAdapter.articleNewsList = article
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val menuItem = menu.findItem(R.id.searchNews)
        if (menuItem != null) {
            menuItem.isVisible = false
        }
    }
}