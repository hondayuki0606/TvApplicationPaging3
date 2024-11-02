package com.example.tvapplicationpaging3.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.tvapplicationpaging3.Movie
import javax.inject.Inject
import kotlin.math.ceil

class MoviesRepository @Inject constructor() {
    private val intList = Array(500) { it }

    fun getMovies(startPosition: Int = 500): Pager<Int, Movie> =
        Pager(
            config = PagingConfig(
                pageSize = 1,
                prefetchDistance = intList.size,
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
    companion object{
        private const val PAGE_SIZE = 1.0
    }
}