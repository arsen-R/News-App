package com.example.newsapp.ui.fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.NewsApplication
import com.example.newsapp.R
import com.example.newsapp.adapter.ArticleNewsAdapter
import com.example.newsapp.databinding.FragmentSearchBinding
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.Resource
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search) {
    private var fragmentBinding: FragmentSearchBinding? = null
    private lateinit var searchView: SearchView

    private val newsAdapter by lazy { ArticleNewsAdapter() }
    private val newsViewModel: NewsViewModel by viewModels {
        NewsViewModelFactory((activity?.application as NewsApplication).repository)
    }

    private fun getLoadData() {
        searchQueryText?.let { newsViewModel.getSearchNews(it, "ua") }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    private var searchQueryText: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSearchBinding.bind(view)
        fragmentBinding = binding

        with(binding.searchArticleRecyclerView) {
            adapter = newsAdapter
            setHasFixedSize(true)
            addOnScrollListener(this@SearchFragment.scrollListener)
        }
        newsAdapter.setOnItemClickListener(onItemClickListener)

        newsViewModel.searchNewsMutableLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideError()
                    response?.data?.let { newsResponse ->
                        if (newsResponse.articleNews.isEmpty()) {
                            showEmptyResultFound()
                        } else {
                            hideEmptyResultFound()
                            newsAdapter.submitList(newsResponse.articleNews.toList())
                        }
                    }
                }
                is Resource.Error -> {
                    hideEmptyResultFound()
                    hideProgressBar()
                    response?.data?.let {
                        binding.searchArticleRecyclerView.removeAllViewsInLayout()
                        showError()
                    }

                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private var isLoading = false
    private var isError = false
    private var isEmptyResultFound = false

    private fun hideEmptyResultFound() {
        fragmentBinding?.messageNoResultFound?.visibility = View.INVISIBLE
        fragmentBinding?.messageModifySearch?.visibility = View.INVISIBLE

        fragmentBinding?.searchArticleRecyclerView?.visibility = View.VISIBLE
        isEmptyResultFound = false
    }
    private fun showEmptyResultFound() {
        fragmentBinding?.messageNoResultFound?.visibility = View.VISIBLE
        fragmentBinding?.messageModifySearch?.visibility = View.VISIBLE
        fragmentBinding?.searchArticleRecyclerView?.visibility = View.INVISIBLE
        isEmptyResultFound = true
    }
    private fun hideError() {
        fragmentBinding?.errorMessageText?.visibility = View.INVISIBLE
        isError = false
    }
    private fun showError() {
        fragmentBinding?.errorMessageText?.visibility = View.VISIBLE
        isError = true
    }
    private fun hideProgressBar() {
        fragmentBinding?.progressBar?.visibility = View.INVISIBLE
        isLoading = false
    }
    private fun showProgressBar() {
        fragmentBinding?.progressBar?.visibility = View.VISIBLE
        isLoading = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_search, menu)
        val menuItem: MenuItem = menu.findItem(R.id.actionSearch)
        val searchManager = (activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager)

        searchView = menuItem.actionView as SearchView
        with(searchView) {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            queryHint = "Search"
            setIconifiedByDefault(false)
            setOnQueryTextListener(searchQuery)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionSearch -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private var job: Job? = null

    private val searchQuery = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            job?.cancel()
            job = MainScope().launch {
                delay(Constants.TIME_LIMIT_FOR_SEARCH)
                query?.let {
                    searchQueryText = it
                    getLoadData()
                }
            }
            return true
        }
        override fun onQueryTextChange(newText: String?): Boolean {
            return true
        }
    }

    private val onItemClickListener = View.OnClickListener { view ->
        val viewHolder= view.tag as RecyclerView.ViewHolder
        val position = viewHolder.adapterPosition

        val articleNews = newsAdapter.getArticleNewsItem(position)

        val action = SearchFragmentDirections.actionSearchFragmentToArticleNewsFragment(articleNews)
        findNavController().navigate(action)
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
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val menuItem = menu.findItem(R.id.searchNews)
        if (menuItem != null) {
            menuItem.isVisible = false
        }
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }
}