package com.example.challengemovieapp.di

import android.content.Context
import com.example.challengemovieapp.data.MovieRepository
import com.example.challengemovieapp.data.api.MovieApiInterface
import com.example.challengemovieapp.util.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApi(networkConnectionInterceptor : NetworkConnectionInterceptor) = MovieApiInterface(networkConnectionInterceptor)

    @Singleton
    @Provides
    fun provideRepository(apiInterface: MovieApiInterface) = MovieRepository(apiInterface)

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context) = context
}