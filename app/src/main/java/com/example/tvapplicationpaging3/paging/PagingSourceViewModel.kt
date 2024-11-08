package com.example.tvapplicationpaging3.paging

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.tvapplicationpaging3.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagingSourceViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {
    private val _pagingDataFlow = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val pagingDataFlow: StateFlow<PagingData<Movie>> = _pagingDataFlow

    fun load(){
        // 最初に仮データを設定
        loadFakeData()

        // 実際のデータを後から取得
        fetchMovies()
    }
    private fun loadFakeData() {
        // 仮データとして100件を設定
        val fakeMovies = List(100) { index ->
            Movie(
                id = index.toLong(), title = "Fake Movie $index",
                description = "description",
                cardImageUrl = MoviePagingSource.IMAGE_URL,
                backgroundImageUrl = MoviePagingSource.ALTERNATE_IMAGE_URL,
            )
        }

        // 仮データをPagingDataに変換して送信
        _pagingDataFlow.value = PagingData.from(fakeMovies)
    }

     private fun fetchMovies() {
        viewModelScope.launch {
            // 実際のデータを取得（例えばAPIから）
            moviesRepository.getMovies().collectLatest { pagingData ->
                // 実際のデータで更新
                _pagingDataFlow.value = pagingData
            }
        }
    }
//    // UIStateクラス
//    // Paging3のデータフロー
//    private val pagingDataFlow = moviesRepository.getMovies().cachedIn(viewModelScope)
//
//    // UIStateで監視するためのStateFlow
//    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
//    val uiState: StateFlow<UIState> = _uiState
//
//    init {
//        Log.d("honda","honda init")
//        observePagingData()
//    }
//
//    private fun observePagingData() {
//        Log.d("honda","honda observePagingData")
//        viewModelScope.launch {
//            pagingDataFlow.collect { pagingData ->
//                Log.d("honda","honda pagingDataFlow.collect")
//                _uiState.value = UIState.Success(pagingData)
//            }
//        }
//    }
}
//sealed class UIState {
//    object Loading : UIState()
//    data class Success(val data: PagingData<Movie>) : UIState()
//    data class Error(val exception: Throwable) : UIState()
//}
