package com.dp.notes.ui.koin.di

import android.util.Log
import com.dp.notes.repository.TestRepository
import com.dp.notes.repository.TestRepositoryImpl
import com.dp.notes.ui.koin.TestKoinViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.dsl.onClose

/**
 * author Dq
 * date on 2022/9/16
 * description 声明Koin要注入的模块
 */

//val testModules = module {
//    viewModel { TestKoinViewModel(get()) }
//    //viewModelOf(::TestKoinViewModel)
//
//    single<TestRepository> { TestRepositoryImpl() }
//    //singleOf(::TestRepositoryImpl) { bind<TestRepository>() }
//}

//val testModules = module {
//    scope<TestKoinActivity> {
//        scoped<TestRepository> {
//            Log.e("hehe", "TestRepository   初始化")
//            TestRepositoryImpl()
//        }.onClose {
//            Log.e("hehe", "TestRepository   销毁")
//        }
//    }
//
//    viewModel {
//        Log.e("hehe", "TestKoinViewModel   初始化1111")
//        TestKoinViewModel(get())
//    }
//}


val testModules = module {
    // 为 ActivityA 定义一个作用域
    scope(named("666666")) {
        scoped<TestRepository> {
            Log.e("hehe", "testModules >>>>>>>> TestRepository   初始化,id=${this.id}   scopeQualifier=${this.scopeQualifier}")
            TestRepositoryImpl()
        }.onClose {

            Log.e("hehe", "testModules >>>>>>>> TestRepository   onClose,id=${this.module.id}   scopeQualifier=${this.scopeQualifier}")
        }
        viewModel {
            //val testRepository = getKoin().getScope(AUTH_SCOPE_NAME).get<TestRepositoryImpl>()
            //TestKoinViewModel(testRepository)
            TestKoinViewModel(get())
        }
    }
}

//val giftModule = module {
//    // === 定义 UserProfileActivity 的 Scope ===
//    scope<GiftPickerFragment> { // 将 Scope 与 UserProfileActivity 类绑定
//        // 在这个 Scope 内，UserProfileRepository 是单例
//        scoped<GiftRepository> {
//            //GiftRepositoryImpl(get())///* 传入构造函数依赖，例如 ApiService */
//            GiftRepositoryImpl()///* 传入构造函数依赖，例如 ApiService */
//        }
//        factory<GiftRepository> { GiftRepositoryImpl() }
//
////        viewModel { BagViewModel(get()) } // get() 将会优先从 UserProfileActivity 的 Scope 找到 UserProfileRepository
////        viewModel { GiftViewModel(get()) } // get() 将会优先从 UserProfileActivity 的 Scope 找到 UserProfileRepository
////        viewModel { SendViewModel(get()) } // get() 将会优先从 UserProfileActivity 的 Scope 找到 UserProfileRepository
//        // ViewModel 也定义在 Activity 的 Scope 内，这样它能访问同一个 Scope 的 Repository
//        // 注意：ViewModel 本身通常由 koin-androidx-viewmodel 管理其生命周期（比 Activity 长），
//        // 但 Koin 允许你在这里定义，确保它创建时能访问到 Activity Scope 内的 scoped 依赖。
//        // 使用 viewModel() 扩展函数依然是推荐方式，Koin 会处理好 Scope 查找。
//        // viewModel { UserProfileViewModel(get()) } // get() 会自动查找当前 Scope 或全局的 UserProfileRepository
////        viewModel { BagViewModel(get()) } // get() 将会优先从 UserProfileActivity 的 Scope 找到 UserProfileRepository
////        viewModel { GiftViewModel(get()) } // get() 将会优先从 UserProfileActivity 的 Scope 找到 UserProfileRepository
////        viewModel { SendViewModel(get()) } // get() 将会优先从 UserProfileActivity 的 Scope 找到 UserProfileRepository
//    }
//    // 或者，如果你想使用命名 Scope (有时更灵活，但不直接绑定类)
//    // scope(named("UserProfileScope")) {
//    //     scoped<UserProfileRepository> { UserProfileRepositoryImpl(get()) }
//    // }
//
//    // === ViewModel 定义（保持不变，Koin 会智能查找依赖）===
//    // ViewModel 会自动从它所关联的 Android 组件（Activity/Fragment）的 Scope 中获取依赖
//    // 如果依赖在 Scope 中找不到，会去全局查找
////    viewModel { BagViewModel(get()) } // get() 将会优先从 UserProfileActivity 的 Scope 找到 UserProfileRepository
////    viewModel { GiftViewModel(get()) } // get() 将会优先从 UserProfileActivity 的 Scope 找到 UserProfileRepository
////    viewModel { SendViewModel(get()) } // get() 将会优先从 UserProfileActivity 的 Scope 找到 UserProfileRepository
//
//    // === 其他全局单例或 Factory 定义 (如果需要) ===
//    // single<ApiService> { createApiService() }
//}

