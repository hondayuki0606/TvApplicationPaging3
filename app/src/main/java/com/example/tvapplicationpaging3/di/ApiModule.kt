package com.example.tvapplicationpaging3.di

//import com.example.tvapplicationpaging3.BuildConfig
import com.example.tvapplicationpaging3.api.PostsApi
import com.example.tvapplicationpaging3.api.UserApi
import com.example.tvapplicationpaging3.infrastructure.ApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    const val BASE_URL = "https://www.yahoo.co.jp/"
    @Provides
    fun provideApiClient(): ApiClient {
        return ApiClient(baseUrl = BASE_URL)
    }

    @Provides
    fun provideUserApi(client: ApiClient): UserApi {
        return client.createService(UserApi::class.java)
    }

    @Provides
    fun providePostsApi(client: ApiClient): PostsApi {
        return client.createService(PostsApi::class.java)
    }
}