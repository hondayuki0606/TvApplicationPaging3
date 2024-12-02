package com.example.tvapplicationpaging3.paging

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.insertSeparators
import androidx.paging.map
import com.example.tvapplicationpaging3.Movie
import com.example.tvapplicationpaging3.paging.MoviePagingSource.Companion.IMAGE_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagingSourceViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {
    data class UiState(
        val pagingDataFlow: PagingData<Movie> = PagingData.empty(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    // UiStateを保持するStateFlow
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState
    private val intList = Array(100) { it }
    val startPosition = 0
    fun load() {
        // 最初に仮データを設定
        loadFakeData()

        // 実際のデータを後から取得
        fetchMovies2()
    }

    private fun loadFakeData() {
        // 仮データとして100件を設定
//        val fakeMovies = List(intList.size) { index ->
//            Movie(
//                id = index.toLong(), title = "Fake Movie $index",
//                description = "description",
//                cardImageUrl = MoviePagingSource.IMAGE_URL,
//                backgroundImageUrl = MoviePagingSource.ALTERNATE_IMAGE_URL,
//            )
//        }
//
//        // 仮データをPagingDataに変換して送信
//        _uiState.update {
//            it.copy(
//                pagingDataFlow = PagingData.from(fakeMovies),
//                isLoading = true,
//                errorMessage = null
//            )
//        }
    }


    //    private fun fetchMovies() {
//        viewModelScope.launch {
//            // 実際のデータを取得（例えばAPIから）
//            moviesRepository.getMovies(startPosition = startPosition, intList = intList)
//                .collectLatest { pagingData ->
//                    // 実際のデータで更新
//                    _uiState.update {
//                        it.copy(
//                            pagingDataFlow = pagingData,
//                            isLoading = true,
//                            errorMessage = null
//                        )
//                    }
//                }
//        }
//    }
    private val _localDataList = MutableStateFlow(listOf<Movie>())
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
                                after
                            } else if (!before.title.first()
                                    .equals(after.title.first(), ignoreCase = true)
                            ) {
                                Log.d(
                                    "honda",
                                    "honda !before.name.first().equals(after.name.first(), ignoreCase = true $before, $after"
                                )
                                // Between two items that start with different letters.
//                            CheeseListItem.Separator(after.title.first())
                                after
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
                _uiState.update {
                    it.copy(
                        pagingDataFlow = pagingData,
                        isLoading = true,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun updateItem(item: Movie) {
        val updateItem = Movie(0, "", "", "", "", "", "")
        val list = _uiState.value.pagingDataFlow
        val list2 = _uiState.value.pagingDataFlow.map { }
        val newList = _localDataList.value.filterNot { it.id == updateItem.id }
//        _localDataList.update {
//            i
//        }

//        _uiState.value.pagingDataFlow.filter { it.id ==  }
        val index = localDataList.indexOf(item)
        val movie = localDataList[index]
        movie.cardImageUrl = IMAGE_URL
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