package com.dp.notes.ui.koin.di

import com.dp.notes.repository.TestRepository
import com.dp.notes.repository.TestRepositoryImpl
import com.dp.notes.ui.koin.TestKoinViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * author Dq
 * date on 2022/9/16
 * description 声明Koin要注入的模块
 */

val viewModelModule = module {
    viewModel { TestKoinViewModel(get()) }
    //viewModelOf(::TestKoinViewModel)
}

val repositoryModule = module {
    single<TestRepository> { TestRepositoryImpl() }
    //singleOf(::TestRepositoryImpl) { bind<TestRepository>() }
}
