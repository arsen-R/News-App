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
import com.example.newsapp.databinding.FragmentHeadlinesItemBinding
import com.example.newsapp.model.ArticleNews
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.Resource
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFactory

class HeadlinesItemFragment : Fragment(R.layout.fragment_headlines_item) {

    private var fragmentBinding: FragmentHeadlinesItemBinding? = null
    private var articleCategory: String? = null
    private val newsAdapter by lazy { ArticleNewsAdapter() }
    private val newsViewModel: NewsViewModel by viewModels {
        NewsViewModelFactory((activity?.application as NewsApplication).repository)
    }

    private val onItemClickListener = View.OnClickListener { view ->
        val viewHolder = view.tag as RecyclerView.ViewHolder
        val position = viewHolder.adapterPosition

        val articleNews: ArticleNews = newsAdapter.getArticleNewsItem(position)

        val action =
            HeadlinesNewsFragmentDirections.actionHeadlinesNewsFragmentToArticleNewsFragment(
                articleNews
            )
        findNavController().navigate(action)
    }
    private var isLoading = false
    private var isError = false


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
            setHasFixedSize(true)
            adapter = newsAdapter
            addOnScrollListener(this@HeadlinesItemFragment.scrollListener)
        }

        newsAdapter.setOnItemClickListener(onItemClickListener)

        getLoadData()

        newsViewModel.newsMutableLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideError()
                    response.data?.let { result ->
                        newsAdapter.submitList(result.articleNews.toList())
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        showError()
                        binding.breakingRecyclerView.removeAllViewsInLayout()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }

        with(binding.swipeToRefresh) {
            setOnRefreshListener {
                showProgressBar()
                getLoadData()
                isRefreshing = false
                hideError()
            }
        }
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

    private fun hideProgressBar() {
        fragmentBinding?.progressBar?.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        fragmentBinding?.progressBar?.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideError() {
        fragmentBinding?.errorMessageText?.visibility = View.INVISIBLE
        isError = false
    }

    private fun showError() {
        fragmentBinding?.errorMessageText?.visibility = View.VISIBLE
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
        articleCategory?.let {
            newsViewModel.getArticleNews(it, "ua")
        }
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }
}