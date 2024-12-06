package com.example.tvapplicationpaging3.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.example.tvapplicationpaging3.Movie
import com.example.tvapplicationpaging3.paging.CheeseListItem
import com.example.tvapplicationpaging3.paging.MoviePagingSource.Companion.IMAGE_URL
import com.example.tvapplicationpaging3.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagingSourceMediatorViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {
    data class MiddleUiState(
        val pagingDataFlow: PagingData<Movie> = PagingData.empty(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    // UiStateを保持するStateFlow
    private val _middleUiState = MutableStateFlow(MiddleUiState())
    val middleUiState: StateFlow<MiddleUiState> = _middleUiState
    private val intList = Array(100) { it }
    val startPosition = 0
    var lastIndex: Int = 0
    fun load() {
        // 最初に仮データを設定
        loadFakeData()

        // 実際のデータを後から取得
        fetchMovies2()
    }

    private fun loadFakeData() {

    }

    private val localDataList = mutableListOf<Movie>()
    fun fetchMovies2() {
        viewModelScope.launch {
            // 実際のデータを取得（例えばAPIから）
            val flow = moviesRepository.getMovies2(startPosition = startPosition, intList = intList)
                .cachedIn(
                    viewModelScope
                ).map { pagingData ->
                    pagingData
                        // Map cheeses to common UI model.
                        .map { movie ->
                            Log.d("honda", "honda $movie")
                            // movieをlocalDataListにaddする
                            localDataList.add(movie)
                            movie
                        }
                        .insertSeparators { before: Movie?, after: Movie? ->
                            if (before == null && after == null) {
                                Log.d(
                                    "honda",
                                    "honda before == null && after == null $before, $after"
                                )
                                // List is empty after fully loaded; return null to skip adding separator.
                                null
                            } else if (after == null) {
                                Log.d("honda", "honda after == null $after")
                                // Footer; return null here to skip adding a footer.
                                null
                            } else if (before == null) {
                                Log.d("honda", "honda before == null $before")
                                // Header
//                            CheeseListItem.Separator(after.title.first())
                                null
                            } else if (!before.title.first()
                                    .equals(after.title.first(), ignoreCase = true)
                            ) {
                                Log.d(
                                    "honda",
                                    "honda !before.name.first().equals(after.name.first(), ignoreCase = true $before, $after"
                                )
                                // Between two items that start with different letters.
//                            CheeseListItem.Separator(after.title.first())
                                null
                            } else {
                                Log.d("honda", "honda else ")
                                // Between two items that start with different letters.
                                // Between two items that start with the same letter.
                                null
                            }
                        }
                }
            flow.collectLatest { pagingData ->
                // 実際のデータで更新
                _middleUiState.update {
                    it.copy(
                        pagingDataFlow = pagingData,
                        isLoading = true,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun targetIndex(item: Movie): Int {
        return localDataList.indexOf(item)
    }

    fun updateItem(index: Int) {
        val updateItem = Movie(0, "", "", IMAGE_URL, IMAGE_URL, "", "")
//        localDataList[index] = updateItem
        localDataList[index].cardImageUrl= IMAGE_URL// = updateItem
//            _uiState.update {
//                it.copy(
//                    pagingDataFlow = list,
//                    isLoading = true,
//                    errorMessage = null
//                )
//            }
    }

    fun getCheeseListItem(): Flow<PagingData<CheeseListItem>> {
        return moviesRepository.getCheeseListItem(
            startPosition = startPosition,
            intList = intList
        )
            .cachedIn(viewModelScope)
    }
}