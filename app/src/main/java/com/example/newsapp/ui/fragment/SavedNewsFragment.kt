package com.example.newsapp.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.NewsApplication
import com.example.newsapp.R
import com.example.newsapp.adapter.SavedArticleAdapter
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
    private var fragmentBinding: FragmentSavedNewsBinding? = null
    private val articleNewsAdapter: SavedArticleAdapter by lazy { SavedArticleAdapter() }

    private val newsViewModel: NewsViewModel by viewModels {
        NewsViewModelFactory((activity?.application as NewsApplication).repository)
    }

    private val onItemClickListener = View.OnClickListener { view: View ->
        val viewHolder = view.tag as RecyclerView.ViewHolder
        val position = viewHolder.layoutPosition

        val articleNews = articleNewsAdapter.getArticleNewsItem(position)

        val action =
            SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleNewsFragment(articleNews)

        findNavController().navigate(action)
    }

    private val onItemTouchHelperCallback = object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val swipeFlags = ItemTouchHelper.LEFT
            return makeMovementFlags(0, swipeFlags)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.layoutPosition
            val articleNews = articleNewsAdapter.getArticleNewsItem(position)
            newsViewModel.deleteArticle(articleNews.title)
            Snackbar.make(
                view!!,
                resources.getString(R.string.delete_article),
                Snackbar.LENGTH_LONG
            ).setAction(resources.getString(R.string.undo)) {
                newsViewModel.saveArticle(articleNews)
            }.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSavedNewsBinding.bind(view)
        fragmentBinding = binding

        with(binding.savedNewsRecyclerView) {
            setHasFixedSize(true)
            adapter = articleNewsAdapter

            ItemTouchHelper(onItemTouchHelperCallback).apply {
                attachToRecyclerView(this@with)
            }
        }
        articleNewsAdapter.setOnItemClickListener(onItemClickListener)

        viewLifecycleOwner.lifecycleScope.launch {
            newsViewModel.getAllSavedArticleFlow.collectLatest { article ->
                if (article.isNullOrEmpty()) {
                    startPageSavedArticles(true)
                } else {
                    startPageSavedArticles(false)
                }
                articleNewsAdapter.submitList(article)
            }
        }
    }

    private fun startPageSavedArticles(isVisible: Boolean) {
        fragmentBinding?.savedIcon?.isVisible = isVisible
        fragmentBinding?.namePage?.isVisible = isVisible
        fragmentBinding?.descriptionPage?.isVisible = isVisible
        fragmentBinding?.savedNewsRecyclerView?.isVisible = !isVisible
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