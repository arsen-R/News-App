package com.example.newsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.NewsApplication
import com.example.newsapp.R
import com.example.newsapp.adapter.ArticleNewsAdapter
import com.example.newsapp.adapter.NewsLoaderStateAdapter
import com.example.newsapp.databinding.FragmentRecentsNewsBinding
import com.example.newsapp.utils.Constants
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class RecentNewsFragment : Fragment(R.layout.fragment_recents_news) {
    private var fragmentBinding: FragmentRecentsNewsBinding? = null
    private val newsAdapter: ArticleNewsAdapter by lazy { ArticleNewsAdapter() }
    private val newsViewModel: NewsViewModel by viewModels {
        NewsViewModelFactory((activity?.application as NewsApplication).repository)
    }
    private val onItemClickListener = View.OnClickListener { view: View ->
        val viewHolder = view.tag as RecyclerView.ViewHolder
        val position = viewHolder.bindingAdapterPosition

        val articleNews = newsAdapter.snapshot()[position]
        val action =
            RecentNewsFragmentDirections.actionRecentNewsFragmentToArticleNewsFragment(articleNews!!)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentRecentsNewsBinding.bind(view)
        fragmentBinding = binding

        with(binding.recentNewsRecyclerView) {
            setHasFixedSize(true)
            adapter = newsAdapter.withLoadStateHeaderAndFooter(
                header = NewsLoaderStateAdapter(),
                footer = NewsLoaderStateAdapter()
            )
        }
        newsAdapter.setOnItemClickListener(onItemClickListener)

        newsAdapter.addLoadStateListener { state ->
            with(binding) {
                recentNewsRecyclerView.isVisible = state.refresh is LoadState.NotLoading
                progressBar.isVisible = state.refresh is LoadState.Loading
                errorMessage.textErrorMessage.isVisible = state.refresh is LoadState.Error
            }
        }
        getLoadData()
        viewLifecycleOwner.lifecycleScope.launch {
            newsViewModel.articleNews.collectLatest { pagingData ->
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
            }
        }
        with(binding.swipeRefresh) {
            setOnRefreshListener {
                newsAdapter.refresh()
                binding.recentNewsRecyclerView.scrollToPosition(0)
                isRefreshing = false
            }
        }
    }

    private fun getLoadData() {
        newsViewModel.setArticleNews(Constants.CATEGORY_TOP)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view?.context!!)
        val country = sharedPreferences.getString("country", "us")
        newsViewModel.setCountry(country)
        newsAdapter.retry()
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentBinding = null
    }
}