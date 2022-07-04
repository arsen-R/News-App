package com.example.newsapp.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.newsapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;


public class ArticleNewsFragment extends Fragment {
    private WebView articleWebView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String link = getArguments().getString("articleNewsLink");
        Log.d("ArticleNews", link);
        articleWebView = view.findViewById(R.id.webView);
        articleWebView.loadUrl(link);
        WebSettings webSettings = articleWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.article_top_bar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuId = item.getItemId();
        switch (menuId) {
            case R.id.shareArticleNews:
                Toast.makeText(getContext(), "Share Article", Toast.LENGTH_LONG).show();
                return true;
            case R.id.saveArticle:
                Snackbar.make(getView(), "Save Article", Snackbar.LENGTH_LONG).show();
                return true;
            case R.id.go_to_original_web_page:
                Toast.makeText(getContext(), "View original web page", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.send_feedback:
                Toast.makeText(getContext(), "Send feedback", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.searchNews);
        if(menuItem!=null) {
            menuItem.setVisible(false);
        }
    }
}