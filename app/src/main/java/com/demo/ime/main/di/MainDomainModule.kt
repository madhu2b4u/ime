package com.demo.ime.main.di

import com.demo.ime.main.data.repository.MainRepository
import com.demo.ime.main.data.repository.MainRepositoryImpl
import com.demo.ime.main.domain.MainUseCase
import com.demo.ime.main.domain.MainUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MainDomainModule {
    @Binds
    abstract fun bindsRepository(
        repoImpl: MainRepositoryImpl
    ): MainRepository

    @Binds
    abstract fun bindsMainUseCase(
        mHomeUseCase: MainUseCaseImpl
    ): MainUseCase
}