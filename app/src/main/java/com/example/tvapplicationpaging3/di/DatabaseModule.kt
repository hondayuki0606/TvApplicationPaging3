package com.example.tvapplicationpaging3.di

import com.example.tvapplicationpaging3.dao.RoomDb
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, RoomDb::class.java, "RoomDb")
        .fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideMovieDao(db: RoomDb) = db.movieDao()

    @Singleton
    @Provides
    fun provideRemoteKeyDao(db: RoomDb) = db.remoteKeyDao()
}