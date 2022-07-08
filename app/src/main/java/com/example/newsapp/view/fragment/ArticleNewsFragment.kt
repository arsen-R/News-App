package com.example.newsapp.view.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.os.Bundle
import android.util.Log
import android.view.*
import com.example.newsapp.R
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.newsapp.NewsApplication
import com.example.newsapp.model.ArticleNews
import com.example.newsapp.view.MainActivity
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFactory
import com.google.android.material.snackbar.Snackbar

class ArticleNewsFragment : Fragment(R.layout.fragment_article_news) {
    private val articleNews: ArticleNews by lazy { arguments?.getSerializable("articleNews") as ArticleNews}

    private val newsViewModel: NewsViewModel by viewModels {
        NewsViewModelFactory((activity?.application as NewsApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val articleWebView: WebView = view.findViewById(R.id.webView)

        with(articleWebView) {
            webViewClient = WebViewClient()
            articleNews?.let { loadUrl(it.link) }
            val webSettings = settings
            webSettings.javaScriptEnabled = true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.article_top_bar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shareArticleNews -> {
                articleNews?.let { shareArticleNews(it.link) }
                return true
            }
            R.id.saveArticle -> {
                newsViewModel.saveArticle(articleNews)
                Snackbar.make(view!!, "Save Article", Snackbar.LENGTH_LONG).show()
                return true
            }
            R.id.go_to_original_web_page -> {
                articleNews?.let { goToOriginalWebPage(it.link) }
                return true
            }
            R.id.send_feedback -> {
                sendFeedback(arrayOf("arsen240302@gmail.com"), "Надіслати відгук")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareArticleNews(link: String) {
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
}