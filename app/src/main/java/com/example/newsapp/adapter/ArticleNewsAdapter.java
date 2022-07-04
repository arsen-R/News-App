package com.example.newsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsapp.R;
import com.example.newsapp.model.ArticleNews;
import com.example.newsapp.model.ResultNews;

import java.util.Locale;

public class ArticleNewsAdapter extends RecyclerView.Adapter<ArticleNewsAdapter.ArticleNewsViewHolder>{
    private ResultNews resultNews;
    private View.OnClickListener onClickListener;

    public ArticleNewsAdapter(ResultNews articleNews) {
        this.resultNews = articleNews;
    }

    @NonNull
    @Override
    public ArticleNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
        return new ArticleNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleNewsViewHolder holder, int position) {
        ArticleNews articleNews = resultNews.getArticleNews().get(position);
        holder.textTitleNews.setText(articleNews.getTitle());
        holder.textSourceNews.setText(articleNews.getSourceId().substring(0,1).toUpperCase(Locale.ROOT) + articleNews.getSourceId().substring(1).toLowerCase(Locale.ROOT));
        holder.textPublishedNews.setText(articleNews.getPubDate());
        Glide.with(holder.imageArticleNews.getContext())
                .load(articleNews.getImageUrl())
                .into(holder.imageArticleNews);
    }

    @Override
    public int getItemCount() {
        return resultNews.getArticleNews().size();
    }

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        onClickListener = onItemClickListener;
    }

    public class ArticleNewsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageArticleNews;
        TextView textTitleNews;
        TextView textSourceNews;
        TextView textPublishedNews;
        public ArticleNewsViewHolder(View view) {
            super(view);
            imageArticleNews = view.findViewById(R.id.imageArticleNews);
            textTitleNews = view.findViewById(R.id.textTitleNews);
            textSourceNews = view.findViewById(R.id.textSourceName);
            textPublishedNews = view.findViewById(R.id.textPublishedNews);
            view.setTag(this);
            view.setOnClickListener(onClickListener);
        }
    }
}
