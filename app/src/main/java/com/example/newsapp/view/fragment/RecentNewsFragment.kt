package com.example.newsapp.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.NewsApplication
import com.example.newsapp.R
import com.example.newsapp.adapter.ArticleNewsAdapter
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.Resource
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFactory

class RecentNewsFragment : Fragment(R.layout.fragment_recents_news) {
    private val articleNewsAdapter: ArticleNewsAdapter by lazy { ArticleNewsAdapter() }
    private lateinit var textMessage: TextView
    private lateinit var progressBar: ProgressBar

    private var isLoading = false
    private var isError = false
    private var isLastPage = false
    private var isScrolling = false

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        isLoading = false
    }
    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        isLoading = true
    }
    private fun hideError() {
        textMessage.visibility = View.INVISIBLE
        isError = false
    }
    private fun showError() {
        textMessage.visibility = View.VISIBLE
        isError = true
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstFirstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotError = !isError
            val isNotIsNotLoadingAndIsLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstFirstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotBeginItem = firstFirstVisibleItemPosition >= 0
            val isTotalMoreCountItem = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotError && isNotIsNotLoadingAndIsLastPage && isAtLastItem && isNotBeginItem
                    && isTotalMoreCountItem && isScrolling

            if (shouldPaginate) {
                newsViewModel.getNewsMutableLiveData("top", "ua")
                isScrolling = false
            }
        }
    }

    private val newsViewModel: NewsViewModel by viewModels {
        NewsViewModelFactory((activity?.application as NewsApplication).repository)
    }

    private val onItemClickListener = View.OnClickListener { view: View ->
        val viewHolder = view.tag as RecyclerView.ViewHolder
        val position = viewHolder.adapterPosition

        val articleNews = articleNewsAdapter.getArticleNewsItem(position)

        val action =
            RecentNewsFragmentDirections.actionRecentNewsFragmentToArticleNewsFragment(articleNews)

        findNavController(view).navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textMessage = view.findViewById(R.id.textMessage)
        progressBar = view.findViewById(R.id.progressBar)

        val recentNewsRecyclerView: RecyclerView = view.findViewById(R.id.recentNewsRecyclerView)
        recentNewsRecyclerView.adapter = articleNewsAdapter
        recentNewsRecyclerView.setHasFixedSize(true)
        recentNewsRecyclerView.addOnScrollListener(this@RecentNewsFragment.scrollListener)

        articleNewsAdapter.setOnItemClickListener(onItemClickListener)

        newsViewModel.getNewsMutableLiveData("top", "ua")
            .observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Success -> {
                        hideProgressBar()
                        hideError()
                        response?.data.let { newsResponse ->
                            newsResponse?.articleNews
                                ?.let { articleNewsAdapter.submitList(it.toList()) }

                            val totalPages = (newsResponse?.totalResults?.div(Constants.QUERY_PAGE_SIZE))
                            isLastPage = newsViewModel.articleNewsPage == totalPages

                            if (isLastPage) {
                                recentNewsRecyclerView.setPadding(0,0,0,0)
                            }
                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        response?.message?.let {
                            showError()
                        }
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            }
    }

}