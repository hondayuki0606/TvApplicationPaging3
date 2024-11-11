package com.example.tvapplicationpaging3.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tvapplicationpaging3.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviePagingSource(
    private val titleList: Array<Int>,
    private val startPosition: Int,
    private val initPagePosition: Int,
    private val pageSize: Int,
) : PagingSource<Int, Movie>() {
    companion object {
        private const val INIT_PAGE_POSITION = -1    // 初期ページのキー
        const val IMAGE_URL =
            "https://www.calm-blog.com/wp-content/uploads/2020/11/cardimage-36-1.png"
        const val ALTERNATE_IMAGE_URL =
            "https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg"
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val position = state.anchorPosition ?: return null
        val prevKey = state.closestPageToPosition(position)?.prevKey
        val nextKey = state.closestPageToPosition(position)?.nextKey

        return prevKey?.plus(1) ?: nextKey?.minus(1)
    }

//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
//        val currentPagePosition = params.key ?: INIT_PAGE_POSITION
//        return try {
//            withContext(Dispatchers.IO) {
//                val movieList = mutableListOf<Movie>()
//                val start = if (currentPagePosition == initPagePosition) {
//                    startPosition
//                } else {
//                    if (currentPagePosition < initPagePosition) {
//                        startPosition - ((initPagePosition - currentPagePosition) * pageSize)
//                    } else {
//                        startPosition + ((currentPagePosition - initPagePosition) * pageSize)
//                    }
//                }
//                for (i in start until start + pageSize) {
//                    if (0 <= i && i < titleList.size) {
//                        // 最初の項目にダミー画像を追加
//                        movieList.add(
//                                Movie(
//                                    title = "test$i fin",
//                                    description = "description",
//                                    cardImageUrl = ALTERNATE_IMAGE_URL,
//                                    backgroundImageUrl = ALTERNATE_IMAGE_URL,
//                                )
//                        )
//                    }
//                }
//                Thread.sleep(5000)
////                fetchMovieAsync(movieList)
////                titleList.forEach { i ->
////                    movieList.add(
////                        Movie(
////                            title = "test${i + 1}",
////                            description = "description",
////                            cardImageUrl = "https://www.calm-blog.com/wp-content/uploads/2020/11/cardimage-36-1.png",
////                            backgroundImageUrl = "https://www.calm-blog.com/wp-content/uploads/2020/11/cardimage-36-1.png",
////                        )
////                    )
////                }
//
//                val prevKey = if (currentPagePosition == 0) {
//                    null
//                } else {
//                    currentPagePosition - 1
//                }
//                val nextKey =
//                    if (movieList.isNullOrEmpty() || start + pageSize > titleList.size) {
//                        null
//                    } else {
//                        currentPagePosition + 1
//                    }
//                return@withContext LoadResult.Page(
//                    data = movieList,
//                    prevKey = prevKey,
//                    nextKey = nextKey
//                )
//            }
//        } catch (e: Exception) {
//            return LoadResult.Error(e)
//        }
//    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key
        return try {
            withContext(Dispatchers.IO) {
                if (page == null) {
                    // at first, return empty data with itemsAfter = 100,
                    // so you immediately get 100 placeholders without delay
                    return@withContext LoadResult.Page(
                        data = emptyList(),
                        prevKey = null,
                        nextKey = 1,
                        itemsBefore = 0,
                        itemsAfter = 100,
                    )
                } else {
                    val start = startPosition * (page - 1)
                    val tmpMovieList = mutableListOf<Movie>()

                    for (i in start until start + pageSize) {
                        if (0 <= i && i < titleList.size) {
                            // 最初の項目にダミー画像を追加
                            val index = (page - 1) * params.loadSize
                            val movie = Movie(
                                title = "test${i + index}",
                                description = "description",
                                cardImageUrl = IMAGE_URL,
                                backgroundImageUrl = ALTERNATE_IMAGE_URL,
                            )
                            tmpMovieList.add(movie)
                        }
                    }
                    // then you always load current page and count correct value for
                    // itemsBefore and itemsAfter
                    fetchMovieAsync(tmpMovieList)
                    return@withContext LoadResult.Page(
                        data = tmpMovieList,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = page + 1,
                        itemsBefore = (page - 1) * params.loadSize,
                        itemsAfter = 100 - page * params.loadSize,
                    )
                }
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
    private fun fetchMovieAsync(movieList: MutableList<Movie>) {
        movieList.forEachIndexed { index, movie ->
//            CoroutineScope(Dispatchers.IO).launch {
            Thread.sleep(5000)
            movie.apply {
                title = "$title fin"
                cardImageUrl = ALTERNATE_IMAGE_URL
            }
//                movie.listener?.complete()
            Log.d("MoviePagingSource", "Image fetch complete for position = $index")
//            }
        }
    }
}