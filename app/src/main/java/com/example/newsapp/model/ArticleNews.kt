package com.example.newsapp.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
@Entity(tableName = "article_news")
data class ArticleNews(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @SerializedName("title")
    @Expose
    val title: String? = null,
    @SerializedName("link")
    @Expose
    val link: String,
    @SerializedName("keywords")
    @Expose
    val keywords: List<String>? = null,
    @SerializedName("creator")
    @Expose
    val creator: List<String>? = null,
    @SerializedName("video_url")
    @Expose
    val videoUrl: String? = null,
    @SerializedName("description")
    @Expose
    val description: String? = null,
    @SerializedName("content")
    @Expose
    val content: String? = null,
    @SerializedName("pubDate")
    @Expose
    val pubDate: String,
    @SerializedName("image_url")
    @Expose
    val imageUrl: String? = null,
    @SerializedName("source_id")
    @Expose
    val sourceId: String,
    @SerializedName("country")
    @Expose
    val country: List<String>,
    @SerializedName("category")
    @Expose
    val category: List<String>,
    @SerializedName("language")
    @Expose
    val language: String
) : Serializable {

    override fun hashCode(): Int {
        var result = title.hashCode()
        if(title.isNullOrEmpty() || keywords.isNullOrEmpty() || description.isNullOrEmpty() || creator.isNullOrEmpty() || imageUrl.isNullOrEmpty() || content.isNullOrEmpty() || videoUrl.isNullOrEmpty()){
            result = 31 * result + title.hashCode()
        }
        return result
    }
}