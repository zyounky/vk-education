package com.example.vkeducation.di

import com.example.vkeducation.data.RemoteAppsRepository
import com.example.vkeducation.domain.repository.AppsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAppsRepository(implementation: RemoteAppsRepository): AppsRepository
}
