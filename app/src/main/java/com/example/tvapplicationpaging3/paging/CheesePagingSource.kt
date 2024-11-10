package com.example.tvapplicationpaging3.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CheesePagingSource(
    private val titleList: Array<Int>,
    private val startPosition: Int,
    private val initPagePosition: Int,
    private val pageSize: Int,
) : PagingSource<Int, Cheese>() {
    companion object {
        private const val INIT_PAGE_POSITION = -1    // 初期ページのキー
        const val IMAGE_URL =
            "https://www.calm-blog.com/wp-content/uploads/2020/11/cardimage-36-1.png"
        const val ALTERNATE_IMAGE_URL =
            "https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg"
    }

    override fun getRefreshKey(state: PagingState<Int, Cheese>): Int? {
        val position = state.anchorPosition ?: return null
        val prevKey = state.closestPageToPosition(position)?.prevKey
        val nextKey = state.closestPageToPosition(position)?.nextKey

        return prevKey?.plus(1) ?: nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cheese> {
        val currentPagePosition = params.key ?: INIT_PAGE_POSITION
        return try {
            withContext(Dispatchers.IO) {
                val cheeseList = mutableListOf<Cheese>()
                val start = if (currentPagePosition == initPagePosition) {
                    startPosition
                } else {
                    if (currentPagePosition < initPagePosition) {
                        startPosition - ((initPagePosition - currentPagePosition) * pageSize)
                    } else {
                        startPosition + ((currentPagePosition - initPagePosition) * pageSize)
                    }
                }
                for (i in start until start + pageSize) {
                    if (0 <= i && i < titleList.size) {
                        // 最初の項目にダミー画像を追加
                        cheeseList.add(
                            Cheese(
                                id = i,
                                name = "Cheese $i"

                            )
                        )
                    }
                }
//                fetchCheeseAsync(cheeseList)

                val prevKey = if (currentPagePosition == 0) {
                    null
                } else {
                    currentPagePosition - 1
                }
                val nextKey =
                    if (cheeseList.isNullOrEmpty() || start + pageSize > titleList.size) {
                        null
                    } else {
                        currentPagePosition + 1
                    }
                return@withContext LoadResult.Page(
                    data = cheeseList,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    private fun fetchCheeseAsync(movieList: MutableList<Cheese>) {
        movieList.forEachIndexed { index, movie ->
            CoroutineScope(Dispatchers.IO).launch {
                Thread.sleep(5000)
                movie.apply {
                    name = "$name fin"
                }
                Log.d("MoviePagingSource", "Image fetch complete for position = $index")
            }
        }
    }
}