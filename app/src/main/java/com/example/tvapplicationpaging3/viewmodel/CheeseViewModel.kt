package com.example.tvapplicationpaging3.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.tvapplicationpaging3.dao.Cheese
import com.example.tvapplicationpaging3.dao.CheeseDao
import com.example.tvapplicationpaging3.paging.CheeseListItem
import com.example.tvapplicationpaging3.paging.ioThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * A simple [AndroidViewModel] that provides a [Flow]<[PagingData]> of delicious cheeses.
 */
class CheeseViewModel(private val dao: CheeseDao) : ViewModel() {
    /**
     * We use the Kotlin [Flow] property available on [Pager]. Java developers should use the
     * RxJava or LiveData extension properties available in `PagingRx` and `PagingLiveData`.
     */
    val allCheeses: Flow<PagingData<CheeseListItem>> = Pager(
        config = PagingConfig(
            /**
             * A good page size is a value that fills at least a few screens worth of content on a
             * large device so the User is unlikely to see a null item.
             * You can play with this constant to observe the paging behavior.
             *
             * It's possible to vary this with list device size, but often unnecessary, unless a
             * user scrolling on a large device is expected to scroll through items more quickly
             * than a small device, such as when the large device uses a grid layout of items.
             */
            pageSize = 60,

            /**
             * If placeholders are enabled, PagedList will report the full size but some items might
             * be null in onBind method (PagedListAdapter triggers a rebind when data is loaded).
             *
             * If placeholders are disabled, onBind will never receive null but as more pages are
             * loaded, the scrollbars will jitter as new pages are loaded. You should probably
             * disable scrollbars if you disable placeholders.
             */
            enablePlaceholders = true,

            /**
             * Maximum number of items a PagedList should hold in memory at once.
             *
             * This number triggers the PagedList to start dropping distant pages as more are loaded.
             */
            maxSize = 200
        )
    ) {
        dao.allCheesesByName()
    }.flow
        .map { pagingData ->
            pagingData
                // Map cheeses to common UI model.
                .map { cheese -> CheeseListItem.Item(cheese) }
                .insertSeparators { before: CheeseListItem?, after: CheeseListItem? ->
                    if (before == null && after == null) {
                        // List is empty after fully loaded; return null to skip adding separator.
                        null
                    } else if (after == null) {
                        // Footer; return null here to skip adding a footer.
                        null
                    } else if (before == null) {
                        // Header
                        CheeseListItem.Separator(after.name.first())
                    } else if (!before.name.first().equals(after.name.first(), ignoreCase = true)){
                        // Between two items that start with different letters.
                        CheeseListItem.Separator(after.name.first())
                    } else {
                        // Between two items that start with the same letter.
                        null
                    }
                }
        }
        .cachedIn(viewModelScope)

    fun insert(text: CharSequence) = ioThread {
        dao.insert(Cheese(id = 0, name = text.toString()))
    }

    fun remove(cheese: Cheese) = ioThread {
        dao.delete(cheese)
    }
}