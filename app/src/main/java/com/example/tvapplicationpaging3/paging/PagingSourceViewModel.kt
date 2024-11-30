package com.example.tvapplicationpaging3.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.tvapplicationpaging3.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
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
    fun fetchMovies2() {
        viewModelScope.launch {
            // 実際のデータを取得（例えばAPIから）
            moviesRepository.getMovies2(startPosition = startPosition, intList = intList).cachedIn(
                viewModelScope
            ).combine(_localDataList) { paging, local ->
                paging.map {
                    if (it.id == local.firstOrNull()?.id) local
                    else it
                }
            }
//

            
        }
    }

    fun updateItem(item: Movie) {
        val updateItem = Movie(0,"","","","","","")
       val list = _uiState.value.pagingDataFlow
        val list2 = _uiState.value.pagingDataFlow.map {  }
        val newList = _localDataList.value.filterNot { it.id == updateItem.id }
//        _localDataList.update {
//            i
//        }
        _localDataList.value = newList
//        _uiState.value.pagingDataFlow.filter { it.id ==  }
        _uiState.update {
            it.copy(
                pagingDataFlow = list,
                isLoading = true,
                errorMessage = null
            )
        }
    }

    fun getCheeseListItem(): Flow<PagingData<CheeseListItem>> {
        return moviesRepository.getCheeseListItem(startPosition = startPosition, intList = intList)
            .cachedIn(viewModelScope)
    }
}