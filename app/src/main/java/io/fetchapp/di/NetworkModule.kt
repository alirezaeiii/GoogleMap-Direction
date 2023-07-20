package io.fetchapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.fetchapp.data.network.DirectionService
import io.fetchapp.util.createNetworkClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideNetworkClient(): Retrofit = createNetworkClient(BASE_URL)

    @Singleton
    @Provides
    fun provideDirectionService(retrofit: Retrofit): DirectionService =
        retrofit.create(DirectionService::class.java)
}

private const val BASE_URL = "https://maps.googleapis.com/maps/"