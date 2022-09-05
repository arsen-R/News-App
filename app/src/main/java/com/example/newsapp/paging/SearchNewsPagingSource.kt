package com.example.newsapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.model.ArticleNews
import com.example.newsapp.network.ArticleNewsService
import com.example.newsapp.utils.Constants
import retrofit2.HttpException

class SearchNewsPagingSource(
    private val apiNewsService: ArticleNewsService,
    private val query: String,
    private val country: String
) : PagingSource<Int, ArticleNews>() {
    override fun getRefreshKey(state: PagingState<Int, ArticleNews>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleNews> {
        if (query.isBlank() || country.isBlank()) {
            return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
        }
        return try {
            val pageNumber = params.key ?: Constants.INITIAL_PAGE_NUMBER
            val response = apiNewsService.searchArticleNews(query, country, pageNumber)
            if (response.isSuccessful) {
                val articleNews = response.body()?.articleNews
                val prevPageNumber = if (pageNumber == 0) null else pageNumber - 1
                val nextPageNumber = if (articleNews?.isEmpty()!!) null else pageNumber + 1
                LoadResult.Page(articleNews, prevPageNumber, nextPageNumber)
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}