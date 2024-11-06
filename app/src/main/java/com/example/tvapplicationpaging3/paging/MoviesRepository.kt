package com.example.tvapplicationpaging3.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.tvapplicationpaging3.Movie
import javax.inject.Inject
import kotlin.math.ceil

class MoviesRepository @Inject constructor() {

    private val intList = Array(500) { it }

    // ページャーを取得
    fun getMovies(startPosition: Int = 81): Pager<Int, Movie> {
        val initPagePosition = calculateInitialKey(startPosition)
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE.toInt(),
                prefetchDistance = 80,
                enablePlaceholders = false,
                initialLoadSize = PAGE_SIZE.toInt()
            ),
            initialKey = initPagePosition,
            pagingSourceFactory = {
                MoviePagingSource(
                    titleList = intList,
                    startPosition = startPosition,
                    initPagePosition = initPagePosition,
                    pageSize = PAGE_SIZE.toInt()
                )
            }
        )
    }

    // 初期キーを計算
    private fun calculateInitialKey(startPosition: Int): Int {
        return if (startPosition == 0) {
            0
        } else {
            ceil(startPosition / PAGE_SIZE).toInt()
        }
    }

    companion object {
        private const val PAGE_SIZE = 20.0
    }
}