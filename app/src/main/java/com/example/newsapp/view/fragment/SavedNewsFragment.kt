package com.example.newsapp.view.fragment

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.NewsApplication
import com.example.newsapp.R
import com.example.newsapp.adapter.ArticleNewsAdapter
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFactory
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
    private val articleNewsAdapter: ArticleNewsAdapter by lazy { ArticleNewsAdapter() }

    private val newsViewModel: NewsViewModel by viewModels {
        NewsViewModelFactory((activity?.application as NewsApplication).repository)
    }

    private val onItemClickListener = View.OnClickListener { view: View ->
        val viewHolder = view.tag as RecyclerView.ViewHolder
        val position = viewHolder.adapterPosition

        val articleNews = articleNewsAdapter.getArticleNewsItem(position)

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

        val onItemTouchHelperCallback = object : ItemTouchHelper.Callback() {
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
                val resource = view.context.resources
                val position = viewHolder.adapterPosition
                val articleNews = articleNewsAdapter.getArticleNewsItem(position)
                newsViewModel.deleteArticleNews(articleNews)
                Snackbar.make(
                    view,
                    resource.getString(R.string.delete_article),
                    Snackbar.LENGTH_LONG
                )
                    .setAction(resource.getString(R.string.undo)) {
                        newsViewModel.saveArticle(articleNews)
                    }
                    .show()
            }
        }

        ItemTouchHelper(onItemTouchHelperCallback).apply {
            attachToRecyclerView(savedNewsRecyclerView)
        }

        newsViewModel.getSavedArticles().observe(viewLifecycleOwner) { article ->
            articleNewsAdapter.submitList(article)
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