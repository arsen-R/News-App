package com.example.newsapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.NewsApplication
import com.example.newsapp.R
import com.example.newsapp.adapter.ArticleNewsAdapter
import com.example.newsapp.model.ArticleNews
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.Resource
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFactory

class HeadlinesItemFragment : Fragment(R.layout.fragment_headlines_item) {
    private lateinit var errorTextMessage: TextView
    private lateinit var progressBar: ProgressBar

    private var isLoading = false
    private var isError = false
    private var isLastPage = false
    private var isScrolling = false

    private var articleCategory: String? = null
    private val newsAdapter by lazy { ArticleNewsAdapter() }

    private val newsViewModel: NewsViewModel by viewModels {
        NewsViewModelFactory((activity?.application as NewsApplication).repository)
    }

    companion object {
        @JvmStatic
        fun newInstance(text: String) =
            HeadlinesItemFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.CATEGORY_PARAM, text)
                }
            }

    }

    private val onItemClickListener = View.OnClickListener { view ->
        val viewHolder = view.tag as RecyclerView.ViewHolder
        val position = viewHolder.adapterPosition

        val articleNews: ArticleNews = newsAdapter.getArticleNewsItem(position)

        val action = HeadlinesNewsFragmentDirections.actionHeadlinesNewsFragmentToArticleNewsFragment(articleNews)
        Navigation.findNavController(view).navigate(action)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            articleCategory = it.getString(Constants.CATEGORY_PARAM)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorTextMessage = view.findViewById(R.id.errorMessageText)
        progressBar = view.findViewById(R.id.progressBar)

        val breakingRecyclerView: RecyclerView = view.findViewById(R.id.breakingRecyclerView)
        breakingRecyclerView.setHasFixedSize(true)
        breakingRecyclerView.adapter = newsAdapter
        breakingRecyclerView.addOnScrollListener(this@HeadlinesItemFragment.scrollListener)
        newsAdapter.setOnItemClickListener(onItemClickListener)

        articleCategory?.let { newsViewModel.getNewsMutableLiveData(it, "ua") }

        newsViewModel.newsMutableLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideError()
                    response.data?.let { result ->
                        newsAdapter.submitList(result.articleNews.toList())

                        val totalPages =
                            (result.totalResults / Constants.QUERY_PAGE_SIZE + 2)

                        isLastPage = newsViewModel.articleNewsPage == totalPages

                        if (isLastPage) {
                            breakingRecyclerView.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        showError()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideError() {
        errorTextMessage.visibility = View.INVISIBLE
        isError = false
    }

    private fun showError() {
        errorTextMessage.visibility = View.VISIBLE
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
            val shouldPaginate =
                isNotError && isNotIsNotLoadingAndIsLastPage && isAtLastItem && isNotBeginItem
                        && isTotalMoreCountItem && isScrolling

            if (shouldPaginate) {
                articleCategory?.let { newsViewModel.getNewsMutableLiveData(it, "ua") }
                isScrolling = false
            }
        }
    }
}