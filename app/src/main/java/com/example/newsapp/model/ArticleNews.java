package com.example.newsapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ArticleNews implements Serializable {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("keywords")
    @Expose
    private List<String> keywords;
    @SerializedName("creator")
    @Expose
    private List<String> creator;
    @SerializedName("video_url")
    @Expose
    private String videoUrl;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("pubDate")
    @Expose
    private String pubDate;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("source_id")
    @Expose
    private String sourceId;
    @SerializedName("country")
    @Expose
    private List<String> country;
    @SerializedName("category")
    @Expose
    private List<String> category;
    @SerializedName("language")
    @Expose
    private String language;

    @Override
    public String toString() {
        return "ArticleNews{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", keywords=" + keywords +
                ", creator=" + creator +
                ", videoUrl='" + videoUrl + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", country=" + country +
                ", category=" + category +
                ", language='" + language + '\'' +
                "}\n";
    }

    public ArticleNews(String title, String link,
                       List<String> keywords, List<String> creator,
                       String videoUrl, String description,
                       String content, String pubDate,
                       String imageUrl, String sourceId,
                       List<String> country, List<String> category, String language) {
        this.title = title;
        this.link = link;
        this.keywords = keywords;
        this.creator = creator;
        this.videoUrl = videoUrl;
        this.description = description;
        this.content = content;
        this.pubDate = pubDate;
        this.imageUrl = imageUrl;
        this.sourceId = sourceId;
        this.country = country;
        this.category = category;
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getCreator() {
        return creator;
    }

    public void setCreator(List<String> creator) {
        this.creator = creator;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public List<String> getCountry() {
        return country;
    }

    public void setCountry(List<String> country) {
        this.country = country;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
