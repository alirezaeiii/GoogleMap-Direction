package io.fetchapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.fetchapp.util.AppDispatchers
import kotlinx.coroutines.Dispatchers

@InstallIn(ViewModelComponent::class)
@Module
object DispatchersModule {

    @Provides
    fun provideAppDispatcher() = AppDispatchers(Dispatchers.Main, Dispatchers.IO)

}