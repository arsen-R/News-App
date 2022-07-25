package com.example.newsapp.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Keep
@Entity(tableName = "news_article")
data class ArticleNews(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("link")
    @Expose
    val link: String,
    @SerializedName("keywords")
    @Expose
    val keywords: List<String>?,
    @SerializedName("creator")
    @Expose
    val creator: List<String>?,
    @SerializedName("video_url")
    @Expose
    val videoUrl: String?,
    @SerializedName("description")
    @Expose
    val description: String?,
    @SerializedName("content")
    @Expose
    val content: String?,
    @SerializedName("pubDate")
    @Expose
    val pubDate: String?,
    @SerializedName("image_url")
    @Expose
    val imageUrl: String?,
    @SerializedName("source_id")
    @Expose
    val sourceId: String?,
    @SerializedName("country")
    @Expose
    val country: List<String>?,
    @SerializedName("category")
    @Expose
    val category: List<String>?,
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