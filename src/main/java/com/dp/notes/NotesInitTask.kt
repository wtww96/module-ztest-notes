package com.dp.notes

import android.content.Context
import android.util.Log
import com.dp.common.task.AppInitManager.Companion.TASK_COMMON
import com.dp.notes.ui.koin.di.testModules
import com.rousetime.android_startup.AndroidStartup
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


/**
 * author Dq
 * date on 2022/11/3
 * description notes module 相关初始化
 */
class NotesInitTask : AndroidStartup<Unit>() {

    override fun create(context: Context) {
        Log.e("startup", " NotesInitTask create    ByName=TASK_COMMON")
        initKoin(context)
    }

    //开启Koin
    private fun initKoin(context: Context) {
        startKoin {
            androidLogger()
            androidContext(context)
            modules(testModules)
        }
    }

    override fun dependenciesByName(): List<String>? = listOf(TASK_COMMON)
    override fun callCreateOnMainThread(): Boolean = false
    override fun waitOnMainThread(): Boolean = false
}