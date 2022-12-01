package com.dp.notes.ui.hilt.di

import com.dp.notes.repository.TestRepository
import com.dp.notes.repository.TestRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * author Dq
 * date on 2022/11/3
 * description di
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(impl: TestRepositoryImpl): TestRepository
}