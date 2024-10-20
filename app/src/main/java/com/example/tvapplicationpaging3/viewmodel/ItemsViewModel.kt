package com.example.tvapplicationpaging3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.tvapplicationpaging3.paging.ItemRemoteMediator

class ItemsViewModel(
) : ViewModel() {

    // 商品情報を取得するRepository
    @Inject
    lateinit var itemsRepository: ItemsRepository
    // お気に入り情報を取得するRepository
    @Inject
    lateinit var favoriteItemsAllRepository: FavoriteItemsRepository
    // APIで取得したリストの情報をキャッシュするDatabase
    @Inject
    lateinit var itemDatabase: ItemDatabase


    // 1画面に表示される商品数
    // pagingの先読み商品数の設定のために定義している
    private val viewIngItemNum = 15

    @OptIn(ExperimentalPagingApi::class)
    val flow = Pager(
        config = PagingConfig(pageSize = viewIngItemNum, initialLoadSize = viewIngItemNum),
        remoteMediator = ItemRemoteMediator(
            itemDatabase, itemsRepository, favoriteItemsRepository
        )
    ) {
        ItemDatabase.ItemDao().pagingSource()
    }.flow.map { pagingData ->
        pagingData.map { it.convert() }
    }.cachedIn(viewModelScope)

    //...
}