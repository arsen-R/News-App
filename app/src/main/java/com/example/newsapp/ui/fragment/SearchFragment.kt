package com.example.newsapp.ui.fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
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
import com.example.newsapp.databinding.FragmentSearchBinding
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.Constants.SEARCH_QUERY_TEXT
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search) {
    private var fragmentBinding: FragmentSearchBinding? = null
    private lateinit var searchView: SearchView
    private val newsAdapter by lazy { ArticleNewsAdapter() }
    private val newsViewModel: NewsViewModel by viewModels {
        NewsViewModelFactory((activity?.application as NewsApplication).repository)
    }
    private var searchQueryText: String? = null
    private fun getLoadData() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view?.context!!)
        val country = sharedPreferences.getString("country", "us")
        newsViewModel.setCountry(country)
    }

    private var isSearchQuery = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (savedInstanceState != null) {
            searchQueryText = savedInstanceState.getString(SEARCH_QUERY_TEXT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSearchBinding.bind(view)
        fragmentBinding = binding

        with(binding.searchArticleRecyclerView) {
            adapter = newsAdapter.withLoadStateHeaderAndFooter(
                header = NewsLoaderStateAdapter(),
                footer = NewsLoaderStateAdapter()
            )
            setHasFixedSize(true)
        }

        newsAdapter.addLoadStateListener { state ->
            with(binding) {
                searchArticleRecyclerView.isVisible = state.refresh is LoadState.NotLoading
                errorMessage.textErrorMessage.isVisible = state.refresh is LoadState.Error
                progressBar.isVisible = state.refresh is LoadState.Loading
                if (state.source.refresh is LoadState.NotLoading && state.append.endOfPaginationReached) {
                    if (isSearchQuery) {
                        if (newsAdapter.itemCount < 1) {
                            searchArticleRecyclerView.isVisible = false
                            val noResultFound : String = context?.getText(R.string.message_no_result_found).toString()
                            Snackbar.make(view, noResultFound , Snackbar.LENGTH_LONG).show()
                        } else {
                            searchArticleRecyclerView.isVisible = true
                        }
                    }
                }
            }

        }
        newsAdapter.setOnItemClickListener(onItemClickListener)
        getLoadData()
        viewLifecycleOwner.lifecycleScope.launch {
            newsViewModel.searchNews.collectLatest { pagingData ->
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
            }
        }
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
            onActionViewExpanded()
            setQuery(searchQueryText, false)
            setOnQueryTextListener(searchQuery)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionSearch -> {
                return false
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
                    newsViewModel.setSearchQuery(it)
                    searchQueryText = it
                    isSearchQuery = true
                    fragmentBinding?.searchArticleRecyclerView?.scrollToPosition(0)
                    searchView.clearFocus()
                }
            }
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY_TEXT, searchQueryText)
    }

    private val onItemClickListener = View.OnClickListener { view ->
        val viewHolder = view.tag as RecyclerView.ViewHolder
        val position = viewHolder.bindingAdapterPosition

        val articleNews = newsAdapter.snapshot()[position]

        val action =
            SearchFragmentDirections.actionSearchFragmentToArticleNewsFragment(articleNews!!)
        findNavController().navigate(action)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val menuItem = menu.findItem(R.id.searchNews)
        if (menuItem != null) {
            menuItem.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
        isSearchQuery = false
        searchView.clearFocus()
    }
}
