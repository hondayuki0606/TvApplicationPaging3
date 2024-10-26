package com.example.tvapplicationpaging3.paging

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.tvapplicationpaging3.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PagingSourceViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
): ViewModel() {
    // Flowの場合
    fun getMoviesAsFlow(): Flow<PagingData<Movie>> =
        moviesRepository.getMovies().flow
}