package com.example.tvapplicationpaging3.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.tvapplicationpaging3.Movie
import javax.inject.Inject

class MoviesRepository @Inject constructor(

) {
    // DBをつかわないバージョン
    fun getMovies(): Pager<Int, Movie> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 1,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                MoviePagingSource()
            },
        )
}