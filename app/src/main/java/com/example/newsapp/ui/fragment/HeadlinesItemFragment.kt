package com.example.newsapp.ui.fragment

import android.os.Bundle
import android.view.View
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
import com.example.newsapp.databinding.FragmentHeadlinesItemBinding
import com.example.newsapp.model.ArticleNews
import com.example.newsapp.utils.Constants
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HeadlinesItemFragment : Fragment(R.layout.fragment_headlines_item) {
    private var fragmentBinding: FragmentHeadlinesItemBinding? = null
    private var articleCategory: String? = null
    private val newsAdapter by lazy { ArticleNewsAdapter() }

    private val newsViewModel: NewsViewModel by viewModels {
        NewsViewModelFactory((activity?.application as NewsApplication).repository)
    }

    private val onItemClickListener = View.OnClickListener { view ->
        val viewHolder = view.tag as RecyclerView.ViewHolder
        val position = viewHolder.bindingAdapterPosition

        val articleNews: ArticleNews? = newsAdapter.snapshot()[position]

        val action =
            HeadlinesNewsFragmentDirections.actionHeadlinesNewsFragmentToArticleNewsFragment(
                articleNews!!
            )
        findNavController().navigate(action)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            articleCategory = it.getString(Constants.CATEGORY_PARAM)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHeadlinesItemBinding.bind(view)
        fragmentBinding = binding


        with(binding.breakingRecyclerView) {
            adapter = newsAdapter.withLoadStateHeaderAndFooter(
                header = NewsLoaderStateAdapter(),
                footer = NewsLoaderStateAdapter()
            )
        }
        newsAdapter.addLoadStateListener { state ->
            with(binding) {
                breakingRecyclerView.isVisible = state.refresh is LoadState.NotLoading
                progressBar.isVisible = state.refresh is LoadState.Loading
                errorMessage.textErrorMessage.isVisible = state.refresh is LoadState.Error
            }
        }
        newsAdapter.setOnItemClickListener(onItemClickListener)

        getLoadData()

        viewLifecycleOwner.lifecycleScope.launch {
                newsViewModel.articleNews.collectLatest { pagingData ->
                    newsAdapter.submitData(lifecycle, pagingData)
            }
        }

        with(binding.swipeToRefresh) {
            setOnRefreshListener {
                newsAdapter.refresh()
                binding.breakingRecyclerView.scrollToPosition(0)
                isRefreshing = false

            }
        }
    }
    private fun getLoadData() {
        newsViewModel.setArticleNews(articleCategory)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view?.context!!)
        val country = sharedPreferences.getString("country", "us")
        newsViewModel.setCountry(country)
        newsAdapter.retry()
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
    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }
}