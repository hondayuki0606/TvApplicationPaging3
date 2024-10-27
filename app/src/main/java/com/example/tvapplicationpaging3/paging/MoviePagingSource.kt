package com.example.tvapplicationpaging3.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tvapplicationpaging3.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviePagingSource(
) : PagingSource<Int, Movie>() {
    // APIのpage指定の最小値
    private val FIRST_INDEX = 0

    // APIの1チャンクあたりの取得データ数
    private val PAGE_SIZE = 30

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val position = state.anchorPosition ?: return null
        val prevKey = state.closestPageToPosition(position)?.prevKey
        val nextKey = state.closestPageToPosition(position)?.nextKey

        return prevKey?.plus(1) ?: nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: FIRST_INDEX

        return try {
            withContext(Dispatchers.IO) {
                val movieList = mutableListOf<Movie>()
                movieList.add(
                    Movie(
                        title = "test$position",
                        description = "description",
                        cardImageUrl = "https://www.calm-blog.com/wp-content/uploads/2020/11/cardimage-36-1.png",
                        backgroundImageUrl = "https://www.calm-blog.com/wp-content/uploads/2020/11/cardimage-36-1.png"
                    )
                )

                val prevKey = if (position >= FIRST_INDEX) position - 1 else null
                val nextKey = if (movieList.isNullOrEmpty()) null else position + 1
                return@withContext LoadResult.Page(
                    data = movieList ?: listOf(),
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}