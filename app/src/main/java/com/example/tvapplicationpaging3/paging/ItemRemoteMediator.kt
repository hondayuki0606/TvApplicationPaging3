package com.example.tvapplicationpaging3.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
//
//@OptIn(ExperimentalPagingApi::class)
//class ItemRemoteMediator(
//    private val database: ItemDatabase,
//    private val itemsRepository: ItemsRepository,
//    private val favoriteItemsRepository: FavoriteItemsRepository
//) : RemoteMediator<Int, ItemEntity>() {
//    override suspend fun load(loadType: LoadType, state: PagingState<Int, ItemEntity>): MediatorResult {
//        val page: Int = // 略 リフレッシュ時には1,追加読み込み時はnextKeys等の読み込むページを指定している
//
//            try {
//                // itemRepository.itemsはRxJavaのSingleで返ってくるので、await()を使ってKotlin コルーチンに変換しています。
//                val result = itemsRepository.items(page).subscribeOn(Schedulers.io()).await().items
//                // APIからの値をローカルDBに格納できるクラスへ変換する
//                val entities = result.convertToEntity()
//                entities.forEach {
//                    // 取得した商品がお気に入りしているかどうかを取得して設定する
//                    it.isFav = favoriteItemsRepository.isFavorite(it.itemId)
//                }
//                val endOfPaginationReached = result.isEmpty()
//                database.withTransaction {
//                    if (loadType === LoadType.REFRESH) {
//                        // PullToRefresh等で、最初から読み込む場合はRoomのテーブルの情報を消す
//                        database.remoteKeysDao().clearRemoteKeys()
//                        database.itemDao().clearAll()
//                    }
//                    val prevKey = if (page == 1) null else page - 1
//                    val nextKey = if (endOfPaginationReached) null else page + 1
//                    val keys = entities.map { entity ->
//                        ItemRemoteKeys(repoId = entity.itemId, prevKey = prevKey, nextKey = nextKey)
//                    }
//                    // Roomのテーブルに取得した商品情報とページ数を格納する
//                    database.remoteKeysDao().insertAll(keys)
//                    database.itemDao().insertAll(entities)
//                }
//                return MediatorResult.Success(endOfPaginationReached)
//            } catch (e: RetrofitException) {
//                return MediatorResult.Error(e)
//            }
//    }
//}