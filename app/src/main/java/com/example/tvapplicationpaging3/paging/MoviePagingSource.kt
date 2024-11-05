package com.example.tvapplicationpaging3.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tvapplicationpaging3.Movie
import com.example.tvapplicationpaging3.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("UNREACHABLE_CODE")
class MoviePagingSource(
    private val titleList: Array<Int>,
    private val startPosition: Int,
    private val initPosition: Int,
    private val pageSize: Int,
) : PagingSource<Int, Movie>() {
    // APIのpage指定の最小値
    private val INIT_INDEX = -1
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val position = state.anchorPosition ?: return null
        val prevKey = state.closestPageToPosition(position)?.prevKey
        val nextKey = state.closestPageToPosition(position)?.nextKey

        return prevKey?.plus(1) ?: nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: INIT_INDEX
        return try {
            withContext(Dispatchers.IO) {
                val movieList = mutableListOf<Movie>()
                val start = if (position == initPosition) {
                    startPosition
                } else {
                    if (position < initPosition) {
                        startPosition - ((initPosition - position) * pageSize)
                    } else {
                        startPosition + ((position - initPosition) * pageSize)
                    }
                }
//                for (i in start until start + pageSize) {
//                    if (0 <= start && i < titleList.size) {
//                        // 最初の項目にダミー画像を追加
//                        movieList.add(
//                            Movie(
//                                title = "test$i",
//                                description = "description",
//                                cardImageUrl = "https://www.calm-blog.com/wp-content/uploads/2020/11/cardimage-36-1.png",
//                                backgroundImageUrl = "https://www.calm-blog.com/wp-content/uploads/2020/11/cardimage-36-1.png",
//                            )
//                        )
//                    }
//                }
                titleList.forEach { i ->
                    movieList.add(
                        Movie(
                            title = "test${i + 1}",
                            description = "description",
                            cardImageUrl = "https://www.calm-blog.com/wp-content/uploads/2020/11/cardimage-36-1.png",
                            backgroundImageUrl = "https://www.calm-blog.com/wp-content/uploads/2020/11/cardimage-36-1.png",
                        )
                    )
                }
                fetchImages(movieList, initPosition)
//                val prevKey = if (position == 0) {
//                    null
//                } else {
//                    position - 1
//                }
//                val nextKey =
//                    if (movieList.isNullOrEmpty() || start + pageSize > titleList.size) {
//                        null
//                    } else {
//                        position + 1
//                    }
                return@withContext LoadResult.Page(
                    data = movieList,
                    prevKey = null,
                    nextKey = null
                )
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    private fun fetchImages(movieList: MutableList<Movie>, initPosition: Int) {
        // ここに画像取得のロジックを追加
        movieList.forEachIndexed { index, movie ->
            CoroutineScope(Dispatchers.IO).launch {
                movie.title = "${movie.title} fin"
                movie.cardImageUrl =
                    "https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg"
                movie.listener?.complete()
                Log.d(
                    "honda", "complete position = $index"
                )
            }
        }
    }
}