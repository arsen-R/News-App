package com.example.newsapp.ui.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.newsapp.NewsApplication
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentArticleNewsBinding
import com.example.newsapp.model.ArticleNews
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFactory
import com.google.android.material.snackbar.Snackbar

class ArticleNewsFragment : Fragment(R.layout.fragment_article_news) {
    private val articleNews: ArticleNews? by lazy { arguments?.getSerializable("articleNews") as ArticleNews }

    private val newsViewModel: NewsViewModel by viewModels {
        NewsViewModelFactory((activity?.application as NewsApplication).repository)
    }

    private var fragmentBinding: FragmentArticleNewsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentArticleNewsBinding.bind(view)
        fragmentBinding = binding

        with(binding.webView) {
            webViewClient = WebViewClient()
            articleNews?.let {
                loadUrl(it.link)
                val webSettings = settings
                webSettings.javaScriptEnabled = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    webSettings.mixedContentMode = MIXED_CONTENT_ALWAYS_ALLOW
                    CookieManager.getInstance().setAcceptThirdPartyCookies(binding.webView, true)
                }
            }

        }
    }

    private var menuItem: MenuItem? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.article_top_bar_menu, menu)
        menuItem = menu.findItem(R.id.saveArticle)
        if (newsViewModel.savedArticleByTitle(articleNews?.title!!) == 1) {
            menuItem?.icon = ContextCompat.getDrawable(activity?.applicationContext!!, R.drawable.ic_bookmark_24)
        } else {
            menuItem?.icon = ContextCompat.getDrawable(activity?.applicationContext!!, R.drawable.ic_bookmark_border_24)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shareArticleNews -> {
                articleNews?.link?.let { shareArticleNews(it) }
                return true
            }
            R.id.saveArticle -> {
                if (newsViewModel.savedArticleByTitle(articleNews?.title!!) != 1) {
                    savedArticle()
                } else {
                    deleteArticle()
                }
                return true
            }
            R.id.go_to_original_web_page -> {
                articleNews?.link?.let { goToOriginalWebPage(it) }
                return true
            }
            R.id.send_feedback -> {
                sendFeedback(
                    arrayOf("arsen240302@gmail.com"),
                    resources.getString(R.string.send_feedback)
                )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun savedArticle() {
        articleNews?.let { newsViewModel.saveArticle(it) }
        menuItem?.icon = ContextCompat.getDrawable(activity?.applicationContext!!, R.drawable.ic_bookmark_24)
        Snackbar.make(
            view!!, resources.getString(R.string.save_article),
            Snackbar.LENGTH_LONG
        ).show()
    }
    private fun deleteArticle() {
        articleNews?.title?.let { newsViewModel.deleteArticle(it) }
        menuItem?.icon = ContextCompat.getDrawable(activity?.applicationContext!!, R.drawable.ic_bookmark_border_24)
        Snackbar.make(
            view!!, resources.getString(R.string.delete_article),
            Snackbar.LENGTH_LONG
        ).setAction(resources.getString(R.string.undo)) {
            savedArticle()
        }.show()
    }
    private fun shareArticleNews(link: String?) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, link)
            type = "text/plain"
        }
        val chooser = Intent.createChooser(intent, "Share")
        try {
            startActivity(chooser)
        } catch (e: ActivityNotFoundException) {
            Log.e("LogArticleNewsError", e.message.toString())
        }

    }

    private fun goToOriginalWebPage(link: String) {
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        try {
            startActivity(webIntent)
        } catch (e: ActivityNotFoundException) {
            Log.d("LogArticleNewsError", e.message.toString())
        }
    }

    private fun sendFeedback(addresses: Array<String>, subject: String) {
        val sendFeedback = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        try {
            startActivity(sendFeedback)
        } catch (e: ActivityNotFoundException) {
            Log.d("LogArticleNewsError", e.message.toString())
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