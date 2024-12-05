package com.example.tvapplicationpaging3.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import com.example.tvapplicationpaging3.Movie
import com.example.tvapplicationpaging3.api.PostsApi
import com.example.tvapplicationpaging3.paging.CheeseListItem
import com.example.tvapplicationpaging3.paging.CheesePagingSource
import com.example.tvapplicationpaging3.paging.MoviePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.math.ceil

class MoviesRepository @Inject constructor(
    private val postsApi: PostsApi
) {

    // ページャーを取得
    fun getMovies(startPosition: Int, intList: Array<Int>): Flow<PagingData<Movie>> {
        val initPagePosition = calculateInitialKey(startPosition)
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE.toInt(),
                prefetchDistance = 10,
                enablePlaceholders = true,
                initialLoadSize = PAGE_SIZE.toInt()
            ),
            initialKey = null,
            pagingSourceFactory = {
                MoviePagingSource(
                    titleList = intList,
                    startPosition = startPosition,
                    initPagePosition = initPagePosition,
                    pageSize = PAGE_SIZE.toInt(),
                    postsApi = postsApi
                )
            }
        ).flow
    }

    // ページャーを取得
    fun getMovies2(startPosition: Int, intList: Array<Int>): Flow<PagingData<Movie>> {
        val initPagePosition = calculateInitialKey(startPosition)
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE.toInt(),
                prefetchDistance = 10,
                enablePlaceholders = true,
                initialLoadSize = PAGE_SIZE.toInt()
            ),
            initialKey = null,
            pagingSourceFactory = {
                MoviePagingSource(
                    titleList = intList,
                    startPosition = startPosition,
                    initPagePosition = initPagePosition,
                    pageSize = PAGE_SIZE.toInt(),
                    postsApi = postsApi
                )
            }
        ).flow
    }

    // ページャーを取得
    fun getCheeseListItem(
        startPosition: Int,
        intList: Array<Int>
    ): Flow<PagingData<CheeseListItem>> {
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
                CheesePagingSource(
                    titleList = intList,
                    startPosition = startPosition,
                    initPagePosition = initPagePosition,
                    pageSize = PAGE_SIZE.toInt()
                )
            }
        ).flow
            .map { pagingData ->
                pagingData
                    // Map cheeses to common UI model.
                    .map { cheese ->
                        Log.d("honda", "honda $cheese")
                        // cheeseを取得した後CheeseListItemに変換して返却
                        CheeseListItem.Item(cheese)
                    }
                    .insertSeparators { before: CheeseListItem?, after: CheeseListItem? ->
                        if (before == null && after == null) {
                            Log.d("honda", "honda before == null && after == null $before, $after")
                            // List is empty after fully loaded; return null to skip adding separator.
                            null
                        } else if (after == null) {
                            Log.d("honda", "honda after == null $after")
                            // Footer; return null here to skip adding a footer.
                            null
                        } else if (before == null) {
                            Log.d("honda", "honda before == null $before")
                            // Header
                            CheeseListItem.Separator(after.name.first())
                        } else if (!before.name.first()
                                .equals(after.name.first(), ignoreCase = true)
                        ) {
                            Log.d("honda", "honda !before.name.first().equals(after.name.first(), ignoreCase = true $before, $after")
                            // Between two items that start with different letters.
                            CheeseListItem.Separator(after.name.first())
                        } else {
                            Log.d("honda", "honda else ")
                            // Between two items that start with different letters.
                            // Between two items that start with the same letter.
                            null
                        }
                    }
            }
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