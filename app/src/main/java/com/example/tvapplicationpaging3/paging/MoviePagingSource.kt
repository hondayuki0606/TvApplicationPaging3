package com.example.tvapplicationpaging3.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tvapplicationpaging3.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviePagingSource(
    private val titleList: ArrayList<String> = arrayListOf(
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "10"
    )
) : PagingSource<Int, Movie>() {
    // APIのpage指定の最小値
    private val INIT_INDEX = 0

    // APIの1チャンクあたりの取得データ数
    private val PAGE_SIZE = 5

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
                if (position == INIT_INDEX) {
                    val start = position * PAGE_SIZE
                    for (i in start until start + PAGE_SIZE)
                        if (i <= titleList.size) {
                            movieList.add(
                                Movie(
                                    title = "test$i",
                                    description = "description",
                                    cardImageUrl = "https://www.calm-blog.com/wp-content/uploads/2020/11/cardimage-36-1.png",
                                    backgroundImageUrl = "https://www.calm-blog.com/wp-content/uploads/2020/11/cardimage-36-1.png"
                                )
                            )
                        }
                } else {
                    var start = 0
                    if (position == INIT_INDEX) {
                        start = INIT_INDEX
                    } else if (INIT_INDEX < position) {
                        start = INIT_INDEX + (position * PAGE_SIZE)
                    }
//                    else if (position < INIT_INDEX) {
//                        100
//                    }
                    for (i in start until start + PAGE_SIZE) {
                        if (i <= titleList.size) {
                            movieList.add(
                                Movie(
                                    title = "test$i",
                                    description = "description",
                                    cardImageUrl = "https://www.calm-blog.com/wp-content/uploads/2020/11/cardimage-36-1.png",
                                    backgroundImageUrl = "https://www.calm-blog.com/wp-content/uploads/2020/11/cardimage-36-1.png"
                                )
                            )
                        }
                    }
                }
                val prevKey = if (position == 0) null else position - 1
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