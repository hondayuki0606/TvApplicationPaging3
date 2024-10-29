package com.example.tvapplicationpaging3.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.tvapplicationpaging3.Movie
import javax.inject.Inject
import kotlin.math.ceil

class MoviesRepository @Inject constructor() {
    val intList = Array(3) { it }

    // DBをつかわないバージョン
    fun getMovies(startPosition: Int = 1): Pager<Int, Movie> =
        Pager(
            config = PagingConfig(
                pageSize = 1,
                prefetchDistance = 1,
                enablePlaceholders = false,
            ),
            initialKey = makeInitKey(startPosition),
            pagingSourceFactory = {
                MoviePagingSource(
                    titleList = intList,
                    startPosition = startPosition,
                    initPosition = makeInitKey(startPosition),
                    pageSize = PAGE_SIZE.toInt()
                )
            },
        )

    private val PAGE_SIZE = 1.0
    private fun makeInitKey(startPosition: Int): Int {
        var initKey = 0.0
        if (0 == startPosition) {
            return initKey.toInt()
        } else {
            val ret = startPosition / PAGE_SIZE
            initKey = ceil(ret)
        }
        return initKey.toInt()
    }
}