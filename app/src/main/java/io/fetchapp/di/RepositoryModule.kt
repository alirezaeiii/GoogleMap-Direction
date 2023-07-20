package io.fetchapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.fetchapp.data.repository.DirectionRepository
import io.fetchapp.repository.DirectionRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    internal abstract fun bindDirectionRepository(movieRepository: DirectionRepositoryImpl): DirectionRepository
}