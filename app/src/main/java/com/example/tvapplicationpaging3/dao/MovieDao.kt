package com.example.tvapplicationpaging3.dao


import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Database Access Object for the Cheese database.
 */
@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<Movie>)

    @Query("SELECT * FROM Movie ORDER BY id ASC")
    fun allSelect(): PagingSource<Int, Movie>

    @Query("DELETE FROM movie")
    suspend fun clearAll()
}