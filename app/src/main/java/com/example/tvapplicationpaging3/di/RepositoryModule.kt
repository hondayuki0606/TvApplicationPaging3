package com.example.tvapplicationpaging3.di

import com.example.tvapplicationpaging3.api.PostsApi
import com.example.tvapplicationpaging3.repository.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideMoviesRepository(postsApi: PostsApi): MoviesRepository {
        return MoviesRepository(postsApi)
    }
}