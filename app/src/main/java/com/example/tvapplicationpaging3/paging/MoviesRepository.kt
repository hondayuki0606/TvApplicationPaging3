package com.example.tvapplicationpaging3.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.tvapplicationpaging3.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.math.ceil

class MoviesRepository @Inject constructor() {

    // ページャーを取得
    fun getMovies(startPosition: Int, intList:Array<Int>): Flow<PagingData<Movie>> {
        val initPagePosition = calculateInitialKey(startPosition)
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE.toInt(),
                prefetchDistance = 1,
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
        ).flow
    }

    // 初期キーを計算
    private fun calculateInitialKey(startPosition: Int): Int {
        var initKey = 0.0
        if (0 == startPosition) {
            return initKey.toInt()
        } else {
            val ret = startPosition / PAGE_SIZE
            initKey = ceil(ret)
        }
        return initKey.toInt()
    }

    companion object {
        private const val PAGE_SIZE = 5.0
    }
}