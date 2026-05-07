package com.example.newapp.di

import com.example.newapp.repository.NewsRepository
import com.example.newapp.repository.NewsRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNewsRepository(
        newsRepositoryImp: NewsRepositoryImp
    ): NewsRepository
}