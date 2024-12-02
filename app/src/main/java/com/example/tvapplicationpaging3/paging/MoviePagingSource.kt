package com.example.tvapplicationpaging3.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tvapplicationpaging3.Movie
import com.example.tvapplicationpaging3.api.PostsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.max

class MoviePagingSource(
    private val titleList: Array<Int>,
    private val startPosition: Int,
    private val initPagePosition: Int,
    private val pageSize: Int,
    private val postsApi: PostsApi
) : PagingSource<Int, Movie>() {
    companion object {
        private const val INIT_PAGE_POSITION = -1    // 初期ページのキー
        const val IMAGE_URL =
            "https://www.calm-blog.com/wp-content/uploads/2020/11/cardimage-36-1.png"
        const val ALTERNATE_IMAGE_URL =
            "https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg"
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        Log.d("", "honda anchorPosition ${state.anchorPosition}")
        Log.d("", "honda state ${state}")
        getRefreshFlg = true
        val anchorPosition = state.anchorPosition ?: return null
        val article = state.closestItemToPosition(anchorPosition) ?: return null
        Log.d("", "honda article state ${article}")
        return state.anchorPosition?.let { anchorPosition ->
            anchorPosition / pageSize
        }
    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
//        val page = params.key
//        return try {
//            withContext(Dispatchers.IO) {
//                if (page == null) {
//                    // at first, return empty data with itemsAfter = 100,
//                    // so you immediately get 100 placeholders without delay
//                    itemsBefore = titleList.size
//                    return@withContext LoadResult.Page(
//                        data = emptyList(),
//                        prevKey = 99,
//                        nextKey = 99,
//                        itemsBefore = itemsBefore,
//                        itemsAfter = 10,
//                    )
//                } else {
//                    // then you always load current page and count correct value for
//                    // itemsBefore and itemsAfter
//                    val start = page
//                    val tmpMovieList = mutableListOf<Movie>()
//                    Thread.sleep(100)
//                    for (i in start until start + pageSize) {
//                        if (0 <= i && i < titleList.size) {
//                            // 最初の項目にダミー画像を追加
//                            val index = (page - 1) * params.loadSize
//                            val movie = Movie(
//                                title = "test${i}",
//                                description = "description",
//                                cardImageUrl = ALTERNATE_IMAGE_URL,
//                                backgroundImageUrl = ALTERNATE_IMAGE_URL,
//                            )
//                            tmpMovieList.add(movie)
//                        }
//                    }
//                    Log.d("honda","honda page $page")
//                    itemsBefore -= 1
//                    return@withContext LoadResult.Page(
//                        data = tmpMovieList,
//                        prevKey = page - 1,
//                        nextKey = page + 1,
//                        itemsAfter = 0,
//                        itemsBefore = itemsBefore,
//                    )
//                }
//            }
//        } catch (e: Exception) {
//            return LoadResult.Error(e)
//        }
//    }

    private var getRefreshFlg = false
    private var itemsBefore = 0
    private var itemsAfter = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key
        return try {
            withContext(Dispatchers.IO) {
                var pagePosition = params.key ?: initPagePosition
                var loadSize = pageSize
                if (params is LoadParams.Refresh) {
                    if (getRefreshFlg) {
                        getRefreshFlg = false
//                        return@withContext LoadResult.Invalid()

                        return@withContext LoadResult.Page(
                            data = emptyList(),
                            prevKey = null,
                            nextKey = null,
                            itemsBefore = 0,
                            itemsAfter = 95,
                        )
                    }
                    // make sure everything visible in the view port is updated / loaded
                    loadSize *= 3
                    pagePosition = max(0, pagePosition - 1)
                }
                if (page == null) {
                    itemsBefore = startPosition
                    itemsAfter = titleList.size - startPosition
                    return@withContext LoadResult.Page(
                        data = emptyList(),
                        prevKey = if (initPagePosition == 0) null else initPagePosition - 1,
                        nextKey = initPagePosition,
                        itemsBefore = itemsBefore,
                        itemsAfter = itemsAfter,
                    )
                } else {
                    // then you always load current page and count correct value for
                    // itemsBefore and itemsAfter
                    val start = if (page == initPagePosition) {
                        startPosition
                    } else {
                        page * pageSize
                    }
//                    val start = 95 - ((20 - page) * pageSize)
                    val tmpMovieList = mutableListOf<Movie>()
                    Thread.sleep(200)
                    for (i in start until start + pageSize) {
                        if (0 <= i && i < titleList.size) {
                            // 最初の項目にダミー画像を追加
                            val index = (page - 1) * params.loadSize
                            val movie = Movie(
                                title = "test${i}",
                                description = "description",
                                cardImageUrl = ALTERNATE_IMAGE_URL,
                                backgroundImageUrl = ALTERNATE_IMAGE_URL,
                            )
                            tmpMovieList.add(movie)
                        }
                    }
                    val prevKey = if (page == 0) null else page - 1
                    val nextKey = if (titleList.size <= page * pageSize) null else page + 1
                    if (page >= initPagePosition) {
                        itemsAfter -= pageSize
                    } else {
                        itemsBefore -= pageSize
                    }
                    Log.d("", "honda prevKey = $prevKey")
                    Log.d("", "honda nextKey = $nextKey")
                    Log.d("", "honda itemsAfter = $itemsAfter")
                    Log.d("", "honda itemsBefore = $itemsBefore")

                    if (params is LoadParams.Refresh) {
                        return@withContext LoadResult.Page(
                            itemsAfter = 0,
                            data = tmpMovieList,
                            prevKey = if (pagePosition == initPagePosition) null else pagePosition - 1,
                            nextKey = nextKey
                        )
                    } else {
                        return@withContext LoadResult.Page(
                            data = tmpMovieList,
                            prevKey = prevKey,
                            nextKey = nextKey,
                            itemsAfter = if (itemsAfter < 0) 0 else itemsAfter,
                            itemsBefore = itemsBefore,
                        )
                    }
                }
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    private suspend fun loadPage(i: Int, index: Int): Movie {
        val movie = Movie(
            title = "test${i + index}",
            description = "description",
            cardImageUrl = IMAGE_URL,
            backgroundImageUrl = ALTERNATE_IMAGE_URL,
        )
        try {
            val test = postsApi.getPosts()
            val ret = test.body()
            Log.d("", "honda ret $ret")
        } catch (e: Exception) {
            Log.d("", "honda ret $e")
        }
        return movie

    }

    private fun fetchMovieAsync(movieList: MutableList<Movie>) {
        movieList.forEachIndexed { index, movie ->
//            CoroutineScope(Dispatchers.IO).launch {
            Thread.sleep(1000)
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