package com.example.newsapp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.NewsApplication
import com.example.newsapp.R
import com.example.newsapp.adapter.ArticleNewsAdapter
import com.example.newsapp.databinding.FragmentRecentsNewsBinding
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.Resource
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFactory

class RecentNewsFragment : Fragment(R.layout.fragment_recents_news) {
    private var fragmentBinding: FragmentRecentsNewsBinding? = null

    private val articleNewsAdapter: ArticleNewsAdapter by lazy { ArticleNewsAdapter() }

    private var isLoading = false
    private var isError = false


    private val newsViewModel: NewsViewModel by viewModels {
        NewsViewModelFactory((activity?.application as NewsApplication).repository)
    }

    private val onItemClickListener = View.OnClickListener { view: View ->
        val viewHolder = view.tag as RecyclerView.ViewHolder
        val position = viewHolder.adapterPosition

        val articleNews = articleNewsAdapter.getArticleNewsItem(position)

        val action =
            RecentNewsFragmentDirections.actionRecentNewsFragmentToArticleNewsFragment(articleNews)

        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentRecentsNewsBinding.bind(view)
        fragmentBinding = binding

        with(binding.recentNewsRecyclerView) {
            adapter = articleNewsAdapter
            setHasFixedSize(true)
            addOnScrollListener(this@RecentNewsFragment.scrollListener)
        }

        articleNewsAdapter.setOnItemClickListener(onItemClickListener)

        getLoadData()

        newsViewModel.newsMutableLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideError()
                    response.data?.let { newsResponse ->
                        articleNewsAdapter.submitList(newsResponse.articleNews.toList())
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response?.message?.let {
                        showError()
                        binding.recentNewsRecyclerView.removeAllViewsInLayout()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }

        with(binding.swipeRefresh) {
            setOnRefreshListener {
                showProgressBar()
                getLoadData()
                isRefreshing = false
                hideError()
            }
        }

    }

    private fun hideProgressBar() {
        fragmentBinding?.progressBar?.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        fragmentBinding?.progressBar?.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideError() {
        fragmentBinding?.textMessage?.visibility = View.INVISIBLE
        isError = false
    }

    private fun showError() {
        fragmentBinding?.textMessage?.visibility = View.VISIBLE
        isError = true
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!isLoading && !isError && !recyclerView.canScrollVertically(1)) {
                isLoading = true
                getLoadData()
            }
        }
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    private fun getLoadData() {
        newsViewModel.getArticleNews(Constants.CATEGORY_TOP, "ua")
    }

    override fun onDestroyView() {
        fragmentBinding = null
        activity?.viewModelStore?.clear()
        super.onDestroyView()
    }
}