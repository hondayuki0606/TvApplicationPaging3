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
        Log.d("", "honda load position = $position")
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
                            title = "test$i",
                            description = "description",
                            cardImageUrl = "https://www.calm-blog.com/wp-content/uploads/2020/11/cardimage-36-1.png",
                            backgroundImageUrl = "https://www.calm-blog.com/wp-content/uploads/2020/11/cardimage-36-1.png",
                            request = i == 4
                        )
                    )
                }
                fetchImages(movieList, position)
                val prevKey = if (position == 0) {
                    null
                } else {
                    position - 1
                }
                val nextKey =
                    if (movieList.isNullOrEmpty() || start + pageSize > titleList.size) {
                        null
                    } else {
                        position + 1
                    }
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

    private fun fetchImages(list: MutableList<Movie>, position: Int) {
        // ここに画像取得のロジックを追加
        list.forEachIndexed { index, it ->
            CoroutineScope(Dispatchers.IO).launch {
                Thread.sleep(2000)

                it.title = "Dispatchers Finish $index"
                it.imageId = R.drawable.app_icon_your_company
                it.listener?.complete()
                it.listener?.complete()

                Log.d(
                    "honda", "comp" +
                            "lete position = $index"
                )
            }
        }
    }
}